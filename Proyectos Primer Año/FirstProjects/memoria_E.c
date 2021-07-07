#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#define max 3

int pedir_datos();
int imprimir_datos();

struct persona {
  char nombre[20];
  char apellido[20];
  int edad;
  long telefono;
} pers[max];

int main() {

  pedir_datos();
  imprimir_datos();
  return 0;
}

int pedir_datos() {

  int i, j = 1;
  for (i = 0; i < max; i++) {
    fflush(stdin);
    printf("Nombre de persona %i: ", j);
    scanf("%s", &pers[i].nombre);
    printf("Apellido de persona %i: ", j);
    scanf("%s", &pers[i].apellido);
    printf("Edad de persona %i: ", j);
    scanf("%i", &pers[i].edad);
    printf("Telefono de persona %i: ", j);
    scanf("%ld", &pers[i].telefono);
    printf("\n"), j++;
  }
  printf("\n");
}

int imprimir_datos() {
  int i, j = 1;
  printf("__Datos alamcenados__\n\n");
  for (i = 0; i < max; i++) {
    printf("Nombre de persona %i: %s\n", j, pers[i].nombre);
    printf("Apellido de persona %i: %s\n", j, pers[i].apellido);
    printf("Edad de persona %i: %i\n", j, pers[i].edad);
    printf("Telefono de persona %i: %ld\n", j, pers[i].telefono);
    printf("\n"), j++;
  }
}
