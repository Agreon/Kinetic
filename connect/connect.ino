int val = 0;

void setup()
{
  Serial.begin(115200); 
}

void loop()
{
  delay(30);
  
  val = analogRead(1);
  Serial.println(val-500);
}
