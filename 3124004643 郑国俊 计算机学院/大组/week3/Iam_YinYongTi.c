#include<stdio.h>
#include<stdlib.h>
#include "sort.c"

// ��������������� 
int *randNumArr(int max, int min, int amount) {
	int *arr = (int*)malloc(amount * sizeof(int));
	int i;
	
	if(max < min) return NULL;
	
	srand((unsigned) time(NULL));
	
	for(i = 0; i < amount; i++) {
		arr[i] = rand() % (max + 1);
		if(arr[i] < min) i--;
	}
	
	return arr;
}

// ������뻺����
void cleanInput(){
	char c;
	while((c = getchar()) != EOF && c != '\n');
} 

void fun1(int *arr, int n) {
	int i, *tempArr;
	int index0=0, index2=n-1, temp;
	
	tempArr = (int*)malloc(n * sizeof(int));
	
	for(i = 0; i < n && i < index2; i++) {
		if(arr[i] == 0) {
			temp = arr[i];
			arr[i] = arr[index0];
			arr[index0++] = temp;
		}
		else if(arr[i] == 2) {
			temp = arr[i];
			arr[i] = arr[index2];
			arr[index2--] = temp;
			i--;
		}
	}
}

int fun2(int *arr, int left, int right, int index) {
	int i=left, j=right, key=arr[i];
	
	while(i < j) {
		while(arr[j] >= key && i < j) {
			j--;
		}
		arr[i] =arr[j];
		while(arr[i] < key && i < j) {
			i++;
		}
		arr[j] = arr[i];
	}
	
	arr[i] = key;
	
	if(i == index-1) return key;
	if(i > index-1) {
		return fun2(arr, left, i, index);
	}
	else {
		return fun2(arr, i+1, right, index);
	}
}

int main() {
	int test1 = 10, test2 = 10;
	int *arr1 = randNumArr(2, 0, test1), *arr2 = randNumArr(100, 10, test2);	// ����������飬���˫�֣�������� 
	int i;
	
	printf("Ӧ����1����ǰ��");
	for(i = 0; i < test1; i++) {
		printf("%d ", arr1[i]);
	}
	printf("\n");
	fun1(arr1, test1);
	printf("�����");
	for(i = 0; i < test1; i++) {
		printf("%d ", arr1[i]);
	}
	printf("\n");
	
	printf("Ӧ����2���飺");
	for(i = 0; i < test2; i++) {
		printf("%d ", arr2[i]);
	}
	printf("\n");
	
	
	printf("��Ҫ�õ��ڼ�С������");
	int index = 0;
	while(!scanf("%d", &index) || index < 1 || index > test2) {
		printf("������һ��λ��1��%d����\n", test2);
		cleanInput();
	}
	
	int number = fun2(arr2, 0, test2-1, index); 	// �ȵó���� 
	printf("��%dС������%d\n", index, number);
	
	quickSort(arr2, 0, test2-1);
	printf("�����\n");
	for(i = 0; i < test2; i++) {
		printf("%d ", arr2[i]);
	}
	printf("\n");	
}
