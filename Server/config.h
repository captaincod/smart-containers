const char* ssid = "realme C11";
const char* password = "";

char* mqtt_broker = "broker.emqx.io";
const int mqtt_port = 1883;

char* TOPIC_STATE = "lab/containers/state/bin3";
char* TOPIC_DATA = "lab/containers/data/bin3";

const int trigPin = 13; 
const int echoPin = 12; 
int duration, distance;
