#include<stdio.h>
#include<stdlib.h>
#include<string.h>

// ��������������� 
int *randNumArr(int max, int min, int amount) {
	int *arr = (int*)malloc(amount * sizeof(int));
	int i;
	
	if(max < min) return NULL;
	
	srand((unsigned) time(NULL));
	
	for(i = 0; i < amount; i++) {
		arr[i] = rand() % max + 1;
		if(arr[i] < min) i--;
	}
	
	return arr;
}

// ������뻺����
void cleanInput(){
	char c;
	while((c = getchar()) != EOF && c != '\n');
} 

int main() {
	int amount, max, min, i;
	int *arr;
	char path[256], input;
	FILE *fp;
	
	while(1) {
		printf("----���ɲ�������----\n"); 
	
		printf("�������������ĳ���\n");
		while(!scanf("%d", &amount)) {
			cleanInput();
			printf("������һ������\n");
		}
		cleanInput();
	
	
		printf("����������������ֵ\n");
		while(!scanf("%d", &max)) {
			cleanInput();
			printf("������һ������\n");
		}
		cleanInput();
	
		printf("���������������Сֵ\n");
		while(!scanf("%d", &min)) {
			cleanInput();
			printf("������һ������\n");
		}
		cleanInput();
	
		printf("������Ҫ������ݵ��ļ����������׺���������ļ����ڣ��������ļ�\n");
		gets(path);
		strcat(path, ".txt");
		fp = fopen(path, "w");
	
		arr = randNumArr(max, min, amount);
		
		fprintf(fp, "%d\n", amount);
	
		for(i = 0; i < amount; i++) {
			fprintf(fp, "%d ", arr[i]);
		}	
	
		fclose(fp);
	
		printf("�ļ���%s���Ѵ���\n", path);
		while(1) {
			printf("�Ƿ�Ҫ������������Y/N��\n");
			input = getchar();
			cleanInput();
			
			if(input == 'Y' || input == 'y') break;
			else if(input == 'N' || input == 'n') return 1;
		}
	}
}

