#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <unistd.h>
#define max 5
#define maxt max *max

void llenar(int tablero[max][max]);
void imprimir(int tablero[max][max]);
void solucion(int tablero[max][max], int x, int y, int pos, int *final);
int valido(int nx, int ny, int tablero[max][max]);

int main() {
  int tablero[max][max];
  int pos = 1;
  int final;
  srand(time(NULL));
  int x = rand() % max;
  int y = rand() % max;
  llenar(tablero);
  tablero[x][y] = pos;
  pos++;
  solucion(tablero, x, y, pos, &final);
  printf("X=%2d y=%2d\n", x, y);
  if (final == 1) {
    printf("\nHay solucion!!\n\n");
    imprimir(tablero);
  } else {
    printf("\nNo hay solucion\n\n");
    imprimir(tablero);
  };
}

void llenar(int tablero[max][max]) { // rellena con 0 el opciones
  int j;
  for (int i = 0; i < max; i++) {
    j = 0;
    for (j = 0; j < max; j++) {
      tablero[i][j] = 0;
    }
  }
}
void imprimir(int tablero[max][max]) {
  int j;
  for (int i = 0; i < max; i++) {
    j = 0;
    for (j = 0; j < max; j++) {
      printf("%3d", tablero[i][j]);
    }
    printf("\n");
  }
  printf("\n\n");
};

void solucion(int tablero[max][max], int x, int y, int pos, int *final) {
  int ix[8] = {-1, -2, -2, -1, 1, 2, 2, 1};
  int jy[8] = {-2, -1, 1, 2, 2, 1, -1, -2};
  *final = 0;
  int mov = 0;
  int nx, ny;
  do {
    nx = x + ix[mov];
    ny = y + jy[mov];
    if (valido(nx, ny, tablero)) {
      tablero[nx][ny] = pos;
      imprimir(tablero);
      sleep(0.1);
      system("cls");
      if (pos == maxt) {
        *final = 1;
      } else {
        solucion(tablero, nx, ny, pos + 1, final);
        if (*final == 0) {
          tablero[nx][ny] = 0;
          imprimir(tablero);
          sleep(0.5);
          system("cls");
          mov++;
        }
      }
    } else {
      mov++;
    }
  } while (*final == 0 && mov < 8);
}

int valido(int nx, int ny, int tablero[max][max]) {
  if ((nx >= 0 && nx < max) && (ny >= 0 && ny < max)) {
    if (tablero[nx][ny] == 0) {
      return 1;
    }
  }
  return 0;
}
