#include<stdio.h>
#include<stdlib.h>
#include<string.h>

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

// 清除输入缓冲区
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
		printf("----生成测试数据----\n"); 
	
		printf("请输入测试数组的长度\n");
		while(!scanf("%d", &amount)) {
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
	
		printf("请输入要存放数据的文件名（无需后缀名），若文件存在，将覆盖文件\n");
		gets(path);
		strcat(path, ".txt");
		fp = fopen(path, "w");
	
		arr = randNumArr(max, min, amount);
		
		fprintf(fp, "%d\n", amount);
	
		for(i = 0; i < amount; i++) {
			fprintf(fp, "%d ", arr[i]);
		}	
	
		fclose(fp);
	
		printf("文件“%s”已创建\n", path);
		while(1) {
			printf("是否要继续创建？（Y/N）\n");
			input = getchar();
			cleanInput();
			
			if(input == 'Y' || input == 'y') break;
			else if(input == 'N' || input == 'n') return 1;
		}
	}
}

