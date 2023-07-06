#include "config.h"
#include <ESP8266WiFi.h>
#include <PubSubClient.h>
#include <ESP8266WebServer.h>


WiFiClient espClient;
PubSubClient client(espClient);
ESP8266WebServer server(80);

void setup() 
{
  Serial.begin(115200);
  delay(100);

  pinMode(trigPin, OUTPUT); 
  pinMode(echoPin, INPUT); 

  Serial.println("Connecting to ");
  Serial.println(ssid);
  
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) 
  {
    delay(1000);
    Serial.print(".");
  }
  Serial.println("");
  Serial.println("WiFi connected..!");
  Serial.print("Got IP: ");  Serial.println(WiFi.localIP());

  server.on("/", handle_OnConnect);
  server.onNotFound(handle_NotFound);

  server.begin();
  Serial.println("HTTP server started");
  
  client.setServer(mqtt_broker, mqtt_port);
  client.setCallback(callback);
  while (!client.connected()) 
  {
    Serial.println("Connecting to MQTT...");
    if (client.connect("ESP8266")){
      Serial.println("connected");
    }
    else{
      Serial.print("failed with state ");
      Serial.println(client.state());
      delay(2000);
    }
  }
  client.publish(TOPIC_STATE, "hello emqx");  
}

void loop() 
{
  server.handleClient();
  client.loop();
}

void callback(char *topic, byte *payload, unsigned int length) {
    Serial.print("Message arrived in topic: ");
    Serial.println(topic);
    Serial.print("With length: ");
    Serial.println(length);    
}

void handle_OnConnect() 
{
  server.send(200, "text/html", SendHTML(distance)); 
}
void handle_NotFound()
{
  server.send(404, "text/plain", "Not found");
}

void get_distance(){
  digitalWrite(trigPin, LOW);
  delayMicroseconds(5);
  digitalWrite(trigPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPin, LOW);
  duration = pulseIn(echoPin, HIGH);
  distance = duration / 58;
  if (distance > 2000)
    distance = 0;
}

String SendHTML(int distance)
{
  get_distance();
  client.publish(TOPIC_DATA, String(distance).c_str());  
  String ptr = "<!DOCTYPE html> <html>\n";
  ptr +="<head><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\">\n";
  ptr +="<title>Smart Container</title>\n";
  ptr +="<style>html { font-family: Helvetica; display: inline-block; margin: 0px auto; text-align: center;}\n";
  ptr +="body{margin-top: 50px;} h1 {color: #444444;margin: 50px auto 30px;} h3 {color: #444444;margin-bottom: 50px;}\n";
  ptr +="p {font-size: 24px;color: #888;margin-bottom: 10px;}\n";
  ptr +="</style>\n";
  ptr +="</head>\n";
  ptr +="<body>\n";
  ptr +="<h1>ESP8266 Web Server</h1>\n";
  ptr +="<h3>" + String(TOPIC_DATA) + "</h3>\n";

  if(distance)
    ptr +="<p>Distance:</p> <p class=\"data\">" + String(distance) + "</p> <p>cm</p>\n";
  else
    ptr +="<p>Distance: Unknown </p>\n";

  ptr += "<script>\n";
  ptr += "function autoRefresh() {\n";
  ptr += "window.location = window.location.href;\n";
  ptr += "}\n";
  ptr += "setInterval('autoRefresh()', 5000);\n";
  ptr += "</script>\n";

  ptr +="</body>\n";
  ptr +="</html>\n";
  return ptr;
}
