#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include<time.h>
#include "sort.c"

// ������뻺����
void cleanInput(){
	char c;
	while((c = getchar()) != EOF && c != '\n');
} 	

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

// ����д���ļ� 
void writeTo(char *filename, int *arr, int n) {
	FILE *fp = fopen(filename, "w");
	int i;
	
	fprintf(fp, "%d\n", n);
	for(i = 0; i < n; i++) {
		fprintf(fp, "%d ", arr[i]);
	}
	
	fclose(fp);
}
	
// ����һ 
void test(int *arr, int n) {
	clock_t startTime, costTime;
	int a = n;
	int *tempArr, *arrArr[5];
	int i;
	
	for(i = 0; i < 5; i++) {
		arrArr[i] = (int*)malloc(n * sizeof(int));		// �����ڴ� 
	}
	tempArr = (int*)malloc(n * sizeof(int));
	
	for(i = 0; i < n; i++) {		// �������� 
		arrArr[0][i] = arr[i];
		arrArr[1][i] = arr[i];
		arrArr[2][i] = arr[i];
		arrArr[3][i] = arr[i];
		arrArr[4][i] = arr[i];
	}
	
	printf("���ڲ��Բ�������\n"); 
	startTime = clock();
	insertSort(arrArr[0], n);
	costTime = clock() - startTime;
	printf("����ʱ�䣺%d ms\n\n", costTime);
	
	printf("���ڲ��Թ鲢����\n");
	startTime = clock();
	mergeSort(arrArr[1], tempArr, 0, n-1);	
	costTime = clock() - startTime;
	printf("����ʱ�䣺%d ms\n\n", costTime);
	
	printf("���ڲ��Կ�������\n");
	startTime = clock();
	quickSort(arrArr[2], 0, n-1);	
	costTime = clock() - startTime;
	printf("����ʱ�䣺%d ms\n\n", costTime);
	
	printf("���ڲ��Լ�������\n");
	startTime = clock();
	countSort(arrArr[3], n);	
	costTime = clock() - startTime;
	printf("����ʱ�䣺%d ms\n\n", costTime);
	
	printf("���ڲ��Ի�������\n");
	startTime = clock();
	radixCountSort(arrArr[4], n);
	costTime = clock() - startTime;
	printf("����ʱ�䣺%d ms\n\n", costTime);
	
	writeTo("insertSort.txt", arrArr[0], n);		// д���ļ� 
	writeTo("mergeSort.txt", arrArr[1], n);
	writeTo("quickSort.txt", arrArr[2], n);
	writeTo("countSort.txt", arrArr[3], n);
	writeTo("radixCountSort.txt", arrArr[4], n);
	
	for(i = 0; i < 5; i++) {	// �ͷ��ڴ� 
		free(arrArr[i]);
	}
	free(tempArr);
}	

// ���Զ� 
void test2() {	
	int selected=0, max=0, min=0, length=0, counts=0;
	clock_t startTime, costTime;
	int i;
	while(1) {
		printf("��ѡ��Ҫ���Ե������㷨\n1-��������\n2-�鲢����\n3-��������\n4-��������\n5-������������\n6-����\n");
		while(!scanf("%d", &selected) || selected < 1 || selected > 6) {
			printf("����������ѡ��");
			cleanInput();
		}
		cleanInput();
		
		if(selected == 6) {
			return;
		}
		
		printf("������Ҫ���ԵĴ���\n");		// ��д������Ե����� 
		while(!scanf("%d", &counts)) {
			printf("������һ������");
			cleanInput();
		}
		
		printf("�������������ĳ���\n");
		while(!scanf("%d", &length)) {
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
		
		int **arrArr = (int**)malloc(counts * sizeof(int *)); // ����һ����ά����
		int *tempArr = (int*)malloc(length * sizeof(int));
		for(i = 0; i < counts; i++) {
			arrArr[i] = randNumArr(max, min, length);	// ��ǰ��������� 
		}
		
		startTime = clock();
		for(i = 0; i < counts; i++) {
			switch(selected) {
				case 1: insertSort(arrArr[i], length); break;
				case 2: mergeSort(arrArr[i], tempArr, 0, length-1); break;
				case 3: quickSort(arrArr[i], 0, length-1); break;
				case 4: countSort(arrArr[i], length); break;
				case 5: insertSort(arrArr[i], length); break;
			}
		}
		costTime = clock() - startTime;
		
		for(i = 0; i < counts; i++) {
			free(arrArr[i]);
		}
		free(tempArr);
		
		printf("������ʱ%d ms\n������������%d\nÿ��������Ϊ%d\n������ݲ�����%d\n��С���ݲ�С��%d\n\n", costTime, counts, length, max, min);
	}
}

int main() {
	int numInput = 0, i, j;
	char input, filename[256];
	FILE *fp;
	
	while(1) {
		printf("------ͷ���ܶ��㷨������------\n");
		printf("��ӭʹ��ͷ���ܶ������㷨������\n");
		printf("��ѡ�����ģʽ��\n1-���ļ����ԣ���������������\n2-����������ݲ���\n3-�˳�\n");
		while(!scanf("%d", &numInput) || numInput < 1 || numInput > 3) {		// ѡ����� 
			printf("����������ѡ��\n");
			cleanInput();
		}
		cleanInput();
		
		if(numInput == 1) {		// ѡ��һ 
			int back = 1;
			while(1) {
				printf("������Ҫ���Ե��ļ���������׺���������Ҫ���������롰back��\n");
				gets(filename);
				
				back =strcmp(filename, "back");	// �ж��Ƿ񷵻� 
				if(back == 0) {
					break;
				}
				
				fp = fopen(filename, "r");	// ��ȡ�ļ� 
				if(fp == NULL) {
					printf("�ļ������ڣ�����������\n");
					continue;
				}
				break;
			}
			if(back == 0) continue;
			
			printf("�ļ���%s����ȡ��\n", filename);
			
			int amount = 0;	// ������ 
			fscanf(fp, "%d\n", &amount);
			
			int *arr = (int*)malloc(amount * sizeof(int));	// ��ȡ������ 
			for(i = 0; i < amount; i++) {
				fscanf(fp, "%d ", &arr[i]);
			}
			fclose(fp);
			
			printf("�ļ���ȡ��ϣ�����ȡ%d������\n", amount);
			test(arr, amount);	// ���� 
		}
		else if(numInput == 2){
			test2();	
		}
		else {
			return 0;
		}
		
	}
}
