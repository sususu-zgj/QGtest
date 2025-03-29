#include<stdio.h>
#include<stdlib.h>
#include<string.h>

// 插入排序，传入一个数组和数组的长度 
void insertSort(int *numArr, int n) {
	int i=0, j=0, temp;
	
	for(i=1; i<n; i++) {
		temp = numArr[i];
		j = i-1;
		
		while(j >= 0 && temp < numArr[j]) {	// 遍历有序部分，直到遇到一个不大于temp的数 
			numArr[j+1] = numArr[j];
			j--;
		}
		
		numArr[j+1] = temp; // 将 temp 插入那个数之前 
	}
}

// 负责将两半排序好的数组合并为一个有序数组 
void merge(int *numArr, int *tempArr, int left, int mid, int right) {
	int i=left, j=left, k=mid+1;
	
	while(i <= mid && k <= right) {	// 将两半数组中的数按循序排序 
		if(numArr[i] < numArr[k]) {
			tempArr[j++] = numArr[i++];
		}
		else {
			tempArr[j++] = numArr[k++];
		}
	}
	while(i <= mid) {		// 将剩下的数放到有序数组的末端 
		tempArr[j++] = numArr[i++];
	}
	while(k <= right) {
		tempArr[j++] = numArr[k++];
	}
	for(i = left; i <= right; i++) {	// 将原数组替换为排序后的数组 
		numArr[i] = tempArr[i];
	}
	
}

// 归并排序
void mergeSort(int *numArr, int *tempArr, int left, int right) {
	int mid = (left + right)/2;
	
	if(left < right) {
		mergeSort(numArr, tempArr, left, mid);
		mergeSort(numArr, tempArr, mid+1, right);
		merge(numArr, tempArr, left, mid, right);
	}
}

// 递归快排 
void quickSort(int *numArr, int left, int right) {
	int i=left, j=right, key=numArr[i];
	
	if(i >= j) return;
	
	while(i < j) {
		while(numArr[j] >= key && i < j) {
			j--;
		}
		numArr[i] = numArr[j];
		while(numArr[i] < key && i < j) {
			i++;
		}
		numArr[j] = numArr[i];
	}
	
	numArr[i] = key;	// 此时i的位置就是基准值应该在的位置 
	
	quickSort(numArr, left, i-1);
	quickSort(numArr, i+1, right);
}

// 计数排序
void countSort(int *numArr, int n) {
	int min=numArr[0], max=numArr[0];
	int i=0, j=0, range=0;
	
	for(i=1; i<n; i++) {		// 计算数值范围 
		if(numArr[i] > max) {
			max = numArr[i];
		}
		if(numArr[i] < min) {
			min = numArr[i];
		}
	}
	range = max - min + 1;
	
	int *tempArr = (int*)malloc(range * sizeof(int));	// 为计数数组分配内存空间 
	if(tempArr == NULL) {
		printf("内存分配失败");
		return;
	}
	memset(tempArr, 0, sizeof(int) * range);
	
	for(i = 0; i < n; i++) {	// 计数 
		tempArr[numArr[i] - min]++;
	}
	
	j = 0;
	for(i = 0; i < range; i++) {	// 将数字按顺序存入原数组 
		while(tempArr[i]--) {
			numArr[j++] = i+min;
		}
	}
	
	free(tempArr); // 释放计数数组内存	
} 

// 获取某个数字的某一位
int getDigit(int num, int digit) {
	int i=0, temp=1;
	
	for(i = 1; i< digit; i++) {
		temp *= 10;
	}
	
	return (num/temp)%10;
} 

// 基数排序
void radixCountSort(int *numArr, int n) {
	int max=numArr[0], maxDigit=0;
	int *buckets[10]; 	// 负责储存不同位数上的数字 
	int i, j, k;
	
	for(i = 1; i < n; i++) {	// 得到数组中的最大数 
		if(numArr[i] > max) {
			max = numArr[i];
		}
	}
	
	while(max != 0) {	// 计算最大位数 
		max /= 10;
		maxDigit++;
	}
	
	for(i = 0; i < 10; i++) {
		buckets[i] = (int*)malloc(n*sizeof(int));
	}
	
	int size[10] = {0};		// 每个桶目前的大小 
	
	for(i = 1; i <= maxDigit; i++) {	// 对每一位数字进行排序 
		for( j = 0; j < n; j++) {	// 确保当位数相同时是按照从小到大排序的 
			int numDigit = getDigit(numArr[j], i);
			buckets[numDigit][size[numDigit]++] = numArr[j]; 
		}
		
		int index = 0;
		for(j = 0; j < 10; j++) {		// 对某位数字排序后，将排序后的数字存入数组 
			for(k = 0; k < size[j]; k++) {
				numArr[index++] = buckets[j][k];
			}
			
			size[j] = 0;
		}
	}
	
	for(i = 0; i < 10; i++) {
		free(buckets[i]);
	}	
	
}


