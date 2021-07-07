#include <stdio.h>

#define max 9
int imprimir_tablero(int tablero[max][max]);
int leer_archivo(FILE *archivo, int tablero[max][max]);
int resolver_sudoku(int tablero[max][max]);
int valido(int tablero[max][max], int ficha, int fila, int columna);
int buscar_lugar(int tablero[max][max], int *fila, int *columna);

int main() {
  FILE *archivo = fopen("fichas_sudoku.txt", "r");
  int tablero[max][max];
  leer_archivo(archivo, tablero);
  if (resolver_sudoku(tablero)) {
    printf("Sudoku resuelto :D\n\n");
    imprimir_tablero(tablero);
  } else {
    printf("Sin solucion\n\n");
    imprimir_tablero(tablero);
  }
}

int imprimir_tablero(int tablero[max][max]) {
  for (int i = 0; i < max; i++) {
    for (int j = 0; j < max; j++) {
      printf("%2d", tablero[i][j]);
      if (j == 2)
        printf(" |");
      if (j == 5)
        printf(" |");
    }
    if (i == 2)
      printf("\n ---------------------");
    if (i == 5)
      printf("\n ---------------------");
    printf("\n");
  }
}

int leer_archivo(FILE *archivo, int tablero[max][max]) {

  if (archivo == NULL) {
    printf("\nERROR, no se pudo abrir el archivo\n");
  } else {
    for (int i = 0; i < max; i++) {
      for (int j = 0; j < max; j++) {
        fscanf(archivo, "%i\t", &tablero[i][j]);
      }
    }
    printf("\n");
  }
}

int resolver_sudoku(int tablero[max][max]) {
  //int wea;
//  scanf("%i",&wea );
  //imprimir_tablero(tablero);
  int i;
  int fila, columna;
  if (buscar_lugar(tablero, &fila, &columna) == 0) {
    return 1;
  }
  for (i = 1; i < max; i++) {
    if (valido(tablero, i, fila, columna)) {
      tablero[fila][columna] = i;
      if (resolver_sudoku(tablero)) {
        return 1;
      }
      tablero[columna][fila] = 0;
    }
  }
  return 0;
}

int buscar_lugar(int tablero[max][max], int *fila, int *columna) {
  int busca_lug = 0;
  int i, j;
  for (i = 0; i < max; i++) {
    for (j = 0; j < max; j++) {
      if (tablero[i][j] == 0) {
        *columna = i;
        *fila = j;
        busca_lug = 1;
        return busca_lug;
      }
    }
  }
  return busca_lug;
}

int valido(int tablero[max][max], int ficha, int fila, int columna) {
  int i, j;
  for (i = 0; i < max; i++) {
    if (tablero[fila][i] == ficha) { // revisa si la ficha ya se encuentra en la columna
      return 0;
    }
  }
  for (i = 0; i < max; i++) {
    if (tablero[i][columna] == ficha) { // revisa si la ficha ya se encuentra en la fila
      return 0;
    }
  }
  // int divide_fila = (fila / 3) * 3;
  //  int divide_columna = (columna / 3) * 3;
  for (i = ((fila / 3) * 3); i < ((fila / 3) * 3) + 3;
       i++) { // revisa si la ficha ya se encuentra en la grilla
    for (j = ((columna / 3) * 3); j < ((columna / 3) * 3) + 3; j++) {
      if (tablero[j][i] == ficha) {
        return 0;
      }
    }
  }
  return 1;
}
