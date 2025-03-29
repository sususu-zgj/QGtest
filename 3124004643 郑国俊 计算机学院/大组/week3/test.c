#include<stdio.h>
#include<stdlib.h>
#include<time.h>
#include "sort.c"

// 清除输入缓冲区
void cleanInput(){
	char c;
	while((c = getchar()) != EOF && c != '\n');
} 	

// 生成随机测试数据 
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

// 用于写入文件 
void writeTo(char *filename, int *arr, int n) {
	FILE *fp = fopen(filename, "w");
	int i;
	
	fprintf(fp, "%d\n", n);
	for(i = 0; i < n; i++) {
		fprintf(fp, "%d ", arr[i]);
	}
	
	fclose(fp);
}
	
// 测试一 
void test(int *arr, int n) {
	clock_t startTime, costTime;
	int a = n;
	int *tempArr, *arrArr[5];
	int i;
	
	for(i = 0; i < 5; i++) {
		arrArr[i] = (int*)malloc(n * sizeof(int));
	}
	tempArr = (int*)malloc(n * sizeof(int));
	
	for(i = 0; i < n; i++) {
		arrArr[0][i] = arr[i];
		arrArr[1][i] = arr[i];
		arrArr[2][i] = arr[i];
		arrArr[3][i] = arr[i];
		arrArr[4][i] = arr[i];
	}
	
	printf("正在测试插入排序\n"); 
	startTime = clock();
	insertSort(arrArr[0], n);
	costTime = clock() - startTime;
	printf("花费时间：%d ms\n", costTime);
	
	printf("正在测试归并排序\n");
	startTime = clock();
	mergeSort(arrArr[1], tempArr, 0, n-1);	
	costTime = clock() - startTime;
	printf("花费时间：%d ms\n", costTime);
	
	printf("正在测试快速排序\n");
	startTime = clock();
	quickSort(arrArr[2], 0, n-1);	
	costTime = clock() - startTime;
	printf("花费时间：%d ms\n", costTime);
	
	printf("正在测试计数排序\n");
	startTime = clock();
	countSort(arrArr[3], n);	
	costTime = clock() - startTime;
	printf("花费时间：%d ms\n", costTime);
	
	printf("正在测试基数排序\n");
	startTime = clock();
	radixCountSort(arrArr[4], n);
	costTime = clock() - startTime;
	printf("花费时间：%d ms\n", costTime);
	
	writeTo("insertSort.txt", arrArr[0], n);
	writeTo("mergeSort.txt", arrArr[1], n);
	writeTo("quickSort.txt", arrArr[2], n);
	writeTo("countSort.txt", arrArr[3], n);
	writeTo("radixCountSort.txt", arrArr[4], n);
	
	for(i = 0; i < 5; i++) {
		free(arrArr[i]);
	}
	free(tempArr);
}	

// 测试二 
void test2() {
	int selected=0, max=0, min=0, length=0, counts=0;
	clock_t startTime, costTime;
	int i;
	while(1) {
		printf("请选择要测试的排序算法\n1-插入排序\n2-归并排序\n3-快速排序\n4-计数排序\n5-基数计数排序\n6-返回\n");
		while(!scanf("%d", &selected) || selected < 1 || selected > 6) {
			printf("请输入以上选项");
			cleanInput();
		}
		cleanInput();
		
		if(selected == 6) {
			return;
		}
		
		printf("请输入要测试的次数\n");
		while(!scanf("%d", &counts)) {
			printf("请输入一个数字");
			cleanInput();
		}
		
		printf("请输入测试数组的长度\n");
		while(!scanf("%d", &length)) {
			cleanInput();
			printf("请输入一个数字\n");
		}
		cleanInput();
	
		printf("请输入测试数的最大值\n");
		while(!scanf("%d", &max)) {
			cleanInput();
			printf("请输入一个数字\n");
		}
		cleanInput();

		printf("请输入测试数的最小值\n");
		while(!scanf("%d", &min)) {
			cleanInput();
			printf("请输入一个数字\n");
		}
		cleanInput();
		
		int **arrArr = (int**)malloc(counts * sizeof(int *)); // 定义一个二维数组
		int *tempArr = (int*)malloc(length * sizeof(int));
		for(i = 0; i < counts; i++) {
			arrArr[i] = randNumArr(max, min, length);	// 提前生成随机数 
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
		
		printf("本次用时%d ms\n共测试组数据%d\n每组数据量为%d\n最大数据不超过%d\n最小数据不小于%d\n", costTime, counts, length, max, min);
	}
}

int main() {
	int numInput = 0, i, j;
	char input, filename[256];
	FILE *fp;
	
	printf("------头发很多算法测试器------\n");
	printf("欢迎使用头发很多排序算法测试器\n");
	printf("请选择测试模式：\n1-单文件测试（将导出排序结果）\n2-随机生成数据测试\n");
	while(!scanf("%d", &numInput) || numInput < 1 || numInput > 2) {
		printf("请输入以上选项\n");
		cleanInput();
	}
	cleanInput();
	
	while(1) {
		if(numInput == 1) {
			while(1) {
				printf("请输入要测试的文件（包含后缀名）\n");
				gets(filename);
				fp = fopen(filename, "r");
			
				if(fp == NULL) {
					printf("文件不存在，请重新输入\n");
					continue;
				}
				break;
			}
			printf("文件“%s”读取中\n", filename);
			
			int amount = 0;
			fscanf(fp, "%d\n", &amount);
			
			int *arr = (int*)malloc(amount * sizeof(int));
			for(i = 0; i < amount; i++) {
				fscanf(fp, "%d ", &arr[i]);
			}
			fclose(fp);
			
			printf("文件读取完毕，共读取%d个数字\n", amount);
			test(arr, amount);
		}
		else {
			test2();	
		}
		
		printf("是否要退出？（Y/N）");
		char input = getchar();
		cleanInput();
			
		if(input == 'Y' || input == 'y') return 1;
		else if(input == 'N' || input == 'n') continue;
	}
}
