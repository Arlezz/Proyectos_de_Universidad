#include <limits.h>
#include <stdio.h>
#include <stdlib.h>
//#define MAX_DIGITOS 30
#define MAX 30
long long int factorial(int);
//void factorial(int n, int resultado[MAX_DIGITOS], int *tam_resultado);

int main() {
        /* Definiendo "n" con solo "int" el maximo valor que puede tomar es n = 12
           Ahora, haciendo modificaciones al tipo de dato y cambiandolo por
           "long long int" que tiene un rango maximo de 19 cifras, soporta
           hasta n = 20 */
        int n;
        printf("INGRESE NUMERO ENTRE <0 - 20> PARA CALCULAR EL FACTORIAL : ");
        scanf("%i", &n);
        long long int valor = factorial(n);
        printf("Factorial de %i = %lli\n", n, valor);
        return 0;
/*-----------------------------------------------------------------------------------------------------------------------------------------------*/
        // int n;
        //int resultado[MAX_DIGITOS];
        //int tam_resultado;
        //printf("INGRESE NUMERO ENTRE <0 - 20> PARA CALCULAR EL FACTORIAL : ");
        //scanf("%i", &n);
        //factorial(n, resultado, &tam_resultado);
}
/*Version iterativa*/
long long int factorial(int n) {

        unsigned long long int resultado = 1;
        for (int i = 2; i <= n; i++) {
                if((i <= n) && (resultado > (ULLONG_MAX/i))) {
                        return 0;
                }else{
                        resultado = resultado * i;
                }
        }
        return (resultado);
}

/*Version recursiva*/
/*long long int factorial(int n) {
        long long int resultado;
        if (n == 1 || n == 0) {
                return 1;
        } else {
                resultado = n * factorial(n - 1);
        }
        return (resultado);
}*/

/* comparando las 2 versiones del codigo, diria que la iterativa se logra
   entender mas a simple vista, aparte que si hubiese algun fallo podria hacer
   el ruteo del algoritmo para saber donde esta el error.
   Por otra parte, el que es recursivo, ayuda mucho a reducir lo que son las
   lineas de codigo y tiene una perspectiva mas compacta y simple */

/*void factorial(int n, int resultado[MAX_DIGITOS], int *tam_resultado) {
        long long int numero_final = 1;
        for (int i = 2; i <= n; i++) {
                if ((i <= n) && (numero_final > (ULLONG_MAX / i))) {
                        printf("0\n");
                } else {
                        numero_final = numero_final * i;
                }
        }
}*/
