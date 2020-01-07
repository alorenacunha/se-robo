#include <SoftwareSerial.h>
#include <Servo.h>
Servo motor;
SoftwareSerial mySerial(0, 1);

int motor1Pin1 = 4; // pin 2 on L293D IC
int motor1Pin2 = 5; // pin 7 on L293D IC
int enable1Pin = 12; // pin 1 on L293D IC
int motor2Pin1 = 2; // pin 10 on L293D IC
int motor2Pin2 = 3; // pin 15 on L293D IC
int enable2Pin = 11; // pin 9 on L293D IC
int dataFromBT;
 //-----------------------------------
//Sensor ULTRASONICO
const int echoPin = 9;
const int trigPin = 8;
const double distancia_ob = 0.20;
//-----------------------------------

void setup() {
  Serial.begin(57600);
  Serial.println("LEDOnOff Starting...");
 
    pinMode(motor1Pin1, OUTPUT);
    pinMode(motor1Pin2, OUTPUT);
    pinMode(enable1Pin, OUTPUT);
    pinMode(motor2Pin1, OUTPUT);
    pinMode(motor2Pin2, OUTPUT);
    pinMode(enable2Pin, OUTPUT);
  // The data rate for the SoftwareSerial port needs to 
  // match the data rate for your bluetooth board.
    digitalWrite(enable1Pin, HIGH);
    digitalWrite(enable2Pin, HIGH);
    //SENSOR ULTRASÔNICO
  pinMode(echoPin, INPUT);
  pinMode(trigPin, OUTPUT);
  //-----------------------------------
  
  pinMode(13, OUTPUT); 
  
  mySerial.begin(115200);
  // motor.attach(12);   
}
 
//Devolve a distância em metros do objeto
double disparaUltraSonico(){
  float tempo, dist;
  //seta o pino 12 com um pulso baixo "LOW" ou desligado ou ainda 0
  digitalWrite(trigPin, LOW);
  // delay de 2 microssegundos
  delayMicroseconds(2);
  //seta o pino 12 com pulso alto "HIGH" ou ligado ou ainda 1
  digitalWrite(trigPin, HIGH);
  delayMicroseconds(10);
  //seta o pino 12 com pulso baixo novamente
  digitalWrite(trigPin, LOW);
  tempo = pulseIn(echoPin, HIGH);
  pulseIn(echoPin, LOW);
  dist = ((tempo * 350)/1000000)/2;
  Serial.print(dist);
  Serial.print(" metros");
  Serial.println();
  return dist;
}

void para_motor()  
{  
  digitalWrite(motor1Pin1, LOW); 
  digitalWrite(motor1Pin2, LOW);
  digitalWrite(motor2Pin1, LOW);
  digitalWrite(motor2Pin2, LOW); 
  delay(500);
}

void frente()  
{  
  digitalWrite(motor1Pin1, LOW); 
  digitalWrite(motor1Pin2, HIGH);
  digitalWrite(motor2Pin1, HIGH);
  digitalWrite(motor2Pin2, LOW); 
  delay(1000);
}
void right()  
{  
  digitalWrite(motor1Pin1, LOW); 
  digitalWrite(motor1Pin2, LOW);
  digitalWrite(motor2Pin1, LOW);
  digitalWrite(motor2Pin2, HIGH); 
  delay(1000);
}
void left()  
{  
  digitalWrite(motor1Pin1, HIGH); 
  digitalWrite(motor1Pin2, LOW);
  digitalWrite(motor2Pin1, LOW);
  digitalWrite(motor2Pin2, LOW); 
  delay(1000);
}
void tras()  
{  
  digitalWrite(motor1Pin1, HIGH); 
  digitalWrite(motor1Pin2, LOW);
  digitalWrite(motor2Pin1, LOW);
  digitalWrite(motor2Pin2, HIGH); 
  delay(1000);
}
 
void loop() {
  if (mySerial.available())
    dataFromBT = mySerial.read();
 
  if (dataFromBT == '0') {
    // Turn on LEFD
    //motor.write(16);
    digitalWrite(13, LOW);
    frente();
    para_motor();
  }
  if (dataFromBT == '1') {
    // Turn off LED
    //motor.write(160);    
    digitalWrite(13, HIGH);
    tras();
    para_motor();
  }
  if (dataFromBT == '2') {
    // Turn off LED
    //motor.write(160);    
    right();
    para_motor();
  }
  if (dataFromBT == '3') {
    // Turn off LED
    //motor.write(160);    
    left();
    para_motor();
  }  
  if (dataFromBT == '4') {
    // Turn off LED
    //motor.write(160); 
    left();
    frente();
    right();
    frente();
    para_motor();
  }
  if (dataFromBT == '5') {
    // Turn off LED
    //motor.write(160); 
    right();
    frente();
    left();
    frente();
    para_motor();
  }
  if (dataFromBT == '6') {
    // Turn off LED
    //motor.write(160); 
    while(true){
    frente();
    digitalWrite(13, LOW);
    if(disparaUltraSonico()<0.2) {  
    digitalWrite(13, HIGH);
    return;
    }
    }
  }
  dataFromBT=-1;
}
