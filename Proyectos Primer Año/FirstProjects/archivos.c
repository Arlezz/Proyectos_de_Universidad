#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>

void crear_file(char base[]);
void agregar_base(char base[]);
void leer_base(char base[]);
void hacer_copia(char base[]);
void borrar_archivo(char base[]);
void ordenar_archivo(char base[]);
void copia_temp_original(char base[], char tex_aux[]);
void copiar_string(char nombre_1[20], char ap_1[20], char am_1[20], char nombre_2[20], char ap_2[20], char am_2[20]);
void contador_lineas(char base[]);

//FILE *archivo, *archivo2, *temp;

int main()
{
	char base[] = "alumnos.txt";
	int eleccion;
	do
	{
		printf("Que desea hacer?\n\n");
		printf("1)Crear archivo.\n");
		printf("2)Agregar base.\n");
		printf("3)Leer archivo.\n");
		printf("4)Hacer copia de seguridad.\n");
		printf("5)Borrar archivo\n");
		printf("6)Ordenar archivo\n");
		printf("0)Salir.\n\n");

		scanf("%i", &eleccion);
		
		switch (eleccion)
		{
		case 1:
			system("clear");
			crear_file(base);
			break;
		case 2:
			system("clear");
			agregar_base(base);
			break;
		case 3:
			system("clear");
			leer_base(base);
			break;
		case 4:
			system("clear");
			hacer_copia(base);
			break;
		case 5:
			system("clear");
			borrar_archivo(base);
			break;
		case 6:
			system("clear");
			ordenar_archivo(base);
			break;
		}
	} while (eleccion != 0);
}

void crear_file(char base[])
{
	FILE *archivo;
	archivo = fopen(base, "w+");

	if (archivo == NULL)
	{
		printf("\nERROR archivo no creardo\n\n");
	}
	else
	{
		system("clear");
		printf("\nArchivo creado :D\n\n");
		sleep(2);
		system("clear");
	}
}

void agregar_base(char base[])
{
	FILE *archivo;
	char nombre[20], ap[20], am[20];
	int r = 1;
	archivo = fopen(base, "a");

	if (archivo == NULL)
	{
		printf("Archivo no existente");
	}
	else
	{
		while (r == 1)
		{
			fflush(stdin);
			printf("Ingrese Nombre: ");
			scanf("%s", nombre);

			printf("Ingrese Apellido Paterno: ");
			scanf("%s", ap);

			printf("Ingrese Apellido Materno: ");
			scanf("%s", am);

			fprintf(archivo, "%s %s %s\n", nombre, ap, am);

			printf("\n¿Desea agregas mas base? (1=Si 0=No)\n\n");
			scanf("%i", &r);
			if (r == 1)
			{
			}
			else
			{
				fclose(archivo);
			}
		}
	}
}

void leer_base(char base[])
{
	FILE *archivo;
	char nombre[20], ap[20], am[20];
	int i = 1;
	archivo = fopen(base, "r");

	if (archivo == NULL)
	{
		printf("\nERROR, no se pudo abrir el archivo\n");
	}
	else
	{
		while (!feof(archivo))
		{
			fscanf(archivo, "%s %s %s", nombre, ap, am);
			if (feof(archivo) == 0)
			{
				printf("%i)%s %s %s\n", i, nombre, ap, am);
				i++;
			}
		}
		printf("\n");
	}
}

void hacer_copia(char base[])
{
	FILE *archivo;
	FILE *archivo2;
	char /*nombre[20], ap[20], am[20], */ n_archivo[20];
	int aux;
	archivo = fopen(base, "r");
	if (archivo == NULL)
	{
		printf("ERROR, archivo no copiado\n");
	}
	else
	{
		printf("Cree nombre del archivo:\n");
		scanf("%s", n_archivo);
	}
	archivo2 = fopen(n_archivo, "w"); //a+ abre un archivo para lectura y escritura
	if (archivo2 == NULL)
	{
		printf("ERROR COPIA FALLIDA\n\n");
	}
	else
	{
		while (!feof(archivo))
		{
			aux = fread(&base, sizeof(*base), 1, archivo);
			if (aux == 0)
			{
				break;
			}
			else
			{
				fwrite(&base, sizeof(*base), 1, archivo2);
			}
		}
		printf("\nCopia Exitosa :D\n\n");
		fclose(archivo);
		fclose(archivo2);
	}
}

void borrar_archivo(char base[])
{
	FILE *archivo;
	int aux = 0;
	archivo = fopen(base, "a");
	if (archivo == NULL)
	{
		printf("ERROR NO SE PUDO BORRAR EL ARCHIVO\n");
	}
	else
	{
		printf("¿Seguro que quiere eliminar el archivo? (1=Si 0=No)\n");
		scanf("%i", &aux);
		fclose(archivo);
	}
	switch (aux)
	{
	case 1:
		if (remove(base) == 0)
		{
			system("clear");
			printf("\nArchivo borrado!!\n\n");
			sleep(2);
			system("clear");
			break;
		}
		else
		{
			printf("No se pudo eliminar el archivo\n\n");
			break;
		}
	case 0:
		break;
	}
}

void ordenar_archivo(char base[])
{
    FILE *archivo;
    FILE *temp;
    char nombre[20], am[20], ap[20];
    char nombre_1[100], am_1[100], ap_1[100];
    char nombre_2[100], am_2[100], ap_2[100];
    int cambio = 1, valor;
    char tex_aux[] = "archivo_temporal.txt";
    nombre_1[0] = '\0';
    am_1[0] = '\0';
    ap_1[0] = '\0';
    archivo = fopen(base, "r");
    temp = fopen(tex_aux, "a");
		if(archivo==NULL){
			printf("Archivo no ordenado\n");
		}
		else{
			printf("Archivo ordenado\n\n");
			
			while ((!feof(archivo)) && cambio != 0)
			 {
				  fscanf(archivo, "%s %s %s\n", nombre_2, ap_2, am_2);
				  if (nombre_1[0] != '\0')
				  {
				      valor = strcmp(nombre_1, nombre_2);
				      if (valor == 0)
				      {
				          valor = strcmp(ap_1, ap_2);
				          if (valor == 0)
				          {
				              valor = strcmp(am_1, am_2);
				              if (valor == 0 || valor < 0)
				              {
				                  fprintf(temp, "%s %s %s\n", nombre_1, ap_1, am_1);
				                  copiar_string(nombre_1, ap_1, am_1, nombre_2, ap_2,am_2);
				              }
				              if (valor > 0)
				              {
				                  fprintf(temp, "%s %s %s\n", nombre_2, ap_2, am_2);
				                  cambio++;
				              }
				          }
				          else
				          {
				              if (valor < 0)
				              {
				                  fprintf(temp, "%s %s %s\n", nombre_1, ap_1, am_1);
				                  copiar_string(nombre_1, ap_1, am_1, nombre_2, ap_2,am_2);
				              }

				              if (valor > 0)
				              {
				                  fprintf(temp, "%s %s %s\n", nombre_2, ap_2, am_2);
				                  cambio++;
				              }
				          }
				      }
				      else
				      {
				          if (valor < 0)
				          {
				              fprintf(temp, "%s %s %s\n", nombre_1, ap_1, am_1);
				              copiar_string(nombre_1, ap_1, am_1, nombre_2, ap_2,am_2);
				          }

				          if (valor > 0)
				          {
				              fprintf(temp, "%s %s %s\n", nombre_2, ap_2, am_2);
				              cambio++;
				          }
				      }
				  }
				  else
				  {
				      copiar_string(nombre_1, ap_1, am_1, nombre_2, ap_2,am_2);
				  }
				  if (feof(archivo))
				  {
				      if (valor < 0)
				      {
				          fprintf(temp, "%s %s %s\n", nombre_2, ap_2, am_2);
				      }
				      else
				      {
				          fprintf(temp, "%s %s %s\n", nombre_1, ap_1, am_1);
				      }
				      if (cambio == 1)
				      {
				          cambio = 0;
				      }

				      if (cambio > 1)
				      {

						cambio = 1;
						fclose(archivo);
						fclose(temp);
						copia_temp_original(base, tex_aux);
						temp = fopen(tex_aux, "w");
						fclose(temp);
						temp = fopen(tex_aux, "a");
						archivo = fopen(base, "r");
						nombre_1[0] = '\0';
						ap_1[0] = '\0';
						am_1[0] = '\0';
				      }
				      else
				      {
				          fclose(temp);
				          remove(tex_aux);
				      }
				  }
			 }
		}
			 
}

void copia_temp_original(char base[], char tex_aux[])
{
	FILE *archivo;
	FILE *temp;
	int aux;
	char nombre[20], ap[20], am[20];
	temp = fopen(tex_aux, "r");
	archivo = fopen(base, "w");

	while (!feof(temp))
	{
		fscanf(temp, "%s %s %s\n", nombre, ap, am);
		fprintf(archivo, "%s %s %s\n", nombre, ap, am);
	}
	fclose(archivo);
	fclose(temp);
	remove(tex_aux);
}

void copiar_string(char nombre_1[20], char ap_1[20], char am_1[20], char nombre_2[20], char ap_2[20], char am_2[20])
{
	strcpy(nombre_1, nombre_2);
	strcpy(ap_1, ap_2);
	strcpy(am_1, am_2);
}

/*void ordenar_dos(char base){
	FILE *archivo;
	FILE *temp1;
	FILE *temp2;

}*/

/*void contador_lineas(char base[]){  intentando aplicar lo aprendido en clases 
	FILE *archivo;	
	int lineas=0;
	char nombre[20],ap[20],am[20];
	archivo=fopen(base,"r");
	while(!feof(archivo)){
		fscanf(archivo,"%s%s%s",nombre,ap,am);
		if(feof(archivo)==0){
			
			lineas++;
		}
	}
	printf("%i\n",lineas);
	fclose(archivo);
}  
*/

//Antony Rdriguez C.
