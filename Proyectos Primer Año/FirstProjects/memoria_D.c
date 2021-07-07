#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#define max 2

int pedir_datos();
int imprimir_datos();

struct persona {
  char nombre[20];
  char *p_nombre;
  char apellido[20];
  char *p_apellido;
  int edad;

} pers[max];

int main() {
  int tamanio;
  pedir_datos(tamanio);
  imprimir_datos();
}

int pedir_datos(int tamanio) {
  int j = 1;
  for (int i = 0; i < max; i++) {
    printf("Nombre de persona %i: ", j);
    scanf("%s", pers[i].nombre);
    tamanio = strlen(pers[i].nombre);
    printf("%i\n", tamanio);
    pers[i].p_nombre = malloc(tamanio * sizeof(char));

    printf("Apellido de persona %i: ", j);
    scanf("%s", pers[i].apellido);
    tamanio = strlen(pers[i].apellido);
    printf("%i\n", tamanio);
    pers[i].p_apellido = malloc(tamanio * sizeof(char));

    printf("Edad de persona %i: ", j);
    scanf("%i", &pers[i].edad);
    j++;
    printf("\n");

    if ((pers[i].p_nombre == NULL) && (pers[i].p_apellido == NULL)) {
      printf("Error de asignacion de memoria\n");
    } else {
      strcpy(pers[i].p_nombre, pers[i].nombre);
      strcpy(pers[i].p_apellido, pers[i].apellido);
    }
  }
  printf("\n");
}

int imprimir_datos() {
  int j = 1;
  for (int i = 0; i < max; i++) {
    printf("Nombre de persona %i: %s\n", j, pers[i].p_nombre);
    printf("Apellido de persona %i: %s\n", j, pers[i].p_apellido);
    printf("Edad de persona %i: %i\n", j, pers[i].edad);
    printf("\n");
    j++;
  }
  printf("---Asignado correctamente---\n\n");
}
