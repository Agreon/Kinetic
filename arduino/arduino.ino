int i = 0;

void setup()
{
  Serial.begin(9600);
}

void loop()
{
   //Serial.print(i,); 
   //Serial.write(i);
   //Serial.print(i,DEC);
   //Serial.print(F("TEST"));
   Serial.print(i);
   //Serial.print("|");
   //Serial.flush();
   delay(15);
   i++;
   if(i > 10000) i = 0;
   //log(i);
}
