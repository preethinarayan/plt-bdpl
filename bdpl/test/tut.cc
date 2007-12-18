#include <stdio.h>
int gcd(int a, int b) 
{
  while (a != b) 
  {
    if (a > b)
      a -= b;
    else
      b -= a;
  }
  return a;
}
main()
{
  printf("the gcd of 294 and 126 is %d\n",gcd(294,126));
}
