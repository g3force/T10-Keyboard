#include <stdio.h>
#include <string.h>

int main(int argc, char argv[]) {
  int posX,posXpadding,posY;
  int i=0;
  FILE * f;
  char input1[20], input2[20];

  printf("pos_x_start=");
  scanf("%d",&posX);
  printf("pos_x_padding=");
  scanf("%d",&posXpadding);
  printf("pos_y=");
  scanf("%d",&posY);

  printf("Format: <keyname><ENTER><keycode><ENTER>\n");
  printf("\ttype \"<\" for ignoring mode.\n");

  do {
    f = fopen("xmlOut","a");
    if(f==NULL) return 1;
    fprintf(f,"<key size_x=\"50\" size_y=\"50\" pos_x=\"%d\" pos_y=\"%d\">\n",posX,posY);
    printf("default:");
    scanf("%s",input1);
    scanf("%s",input2);
    fprintf(f,"\t<mode name=\"default\" keycode=\"%s\">%s</mode>\n",input2,input1);

    printf("shift:");
    scanf("%s",input1);
    if(strcmp(input1,"<")!=0) {
      scanf("%s",input2);
      fprintf(f,"\t<mode name=\"shift\" keycode=\"%s\">%s</mode>\n",input2,input1);

      printf("altgr:");
      scanf("%s",input1);
      if(strcmp(input1,"<")!=0) {
        if(input1!="<") {
          scanf("%s",input2);
          fprintf(f,"\t<mode name=\"altgr\" keycode=\"%s\">%s</mode>\n",input2,input1);
        }
      }
    }
    fprintf(f,"</key>\n");
    fclose(f);
    i++;
    posX+=posXpadding+50;
  } while(1);
//  printf("%d %d",posXstart,posXpadding);
  
}
