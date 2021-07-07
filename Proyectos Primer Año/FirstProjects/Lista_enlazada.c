#include <stdio.h>
#include <stdlib.h>

typedef struct lista {
  int valor;
  struct lista *sig;
} * nodo;

void agregar_Datos(nodo *aux, int e);
void imprimirLista(nodo aux);
void borrarLista(nodo *aux);

int main() {
  int e;
  nodo aux;
  aux = NULL;

  printf("Ingrese elementos, '-1' para terminar: ");
  scanf("%d", &e);
  while (e != -1) {
    agregar_Datos(&aux, e);
    printf("Ingresado correctamente");
    printf("\n");
    printf("Ingrese datos, -1 para terminar: ");
    scanf("%d", &e);
  }
  e = -1;
  printf("\nLos datos de la lista son: ");
  imprimirLista(aux);

  while (e != 1 || e != 0) {
    printf("\n\nDesea eliminar la lista? Y= 1 / N= 0\n");
    scanf("%d", &e);
    if (e == 1) {
      borrarLista(&aux);
      printf("\nSe borro la lista...\n");
      break;
    }break;
  }
  return 0;
}

void agregar_Datos(nodo *aux, int e) {
  nodo N_aux;
  N_aux = malloc(sizeof(nodo));
  N_aux->valor = e;
  N_aux->sig = *aux;
  *aux = N_aux;
}

void imprimirLista(nodo aux) {
  while (aux != NULL) {
    printf("%3d", aux->valor);
    aux = aux->sig;
  }
}

void borrarLista(nodo *aux) {
  nodo actual;
  while (*aux != NULL) {
    actual = *aux;
    *aux = (*aux)->sig;
    free(actual);
  }
}
