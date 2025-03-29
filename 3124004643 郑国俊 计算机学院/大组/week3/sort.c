#include<stdio.h>
#include<stdlib.h>
#include<string.h>

// �������򣬴���һ�����������ĳ��� 
void insertSort(int *numArr, int n) {
	int i=0, j=0, temp;
	
	for(i=1; i<n; i++) {
		temp = numArr[i];
		j = i-1;
		
		while(j >= 0 && temp < numArr[j]) {	// �������򲿷֣�ֱ������һ��������temp���� 
			numArr[j+1] = numArr[j];
			j--;
		}
		
		numArr[j+1] = temp; // �� temp �����Ǹ���֮ǰ 
	}
}

// ������������õ�����ϲ�Ϊһ���������� 
void merge(int *numArr, int *tempArr, int left, int mid, int right) {
	int i=left, j=left, k=mid+1;
	
	while(i <= mid && k <= right) {	// �����������е�����ѭ������ 
		if(numArr[i] < numArr[k]) {
			tempArr[j++] = numArr[i++];
		}
		else {
			tempArr[j++] = numArr[k++];
		}
	}
	while(i <= mid) {		// ��ʣ�µ����ŵ����������ĩ�� 
		tempArr[j++] = numArr[i++];
	}
	while(k <= right) {
		tempArr[j++] = numArr[k++];
	}
	for(i = left; i <= right; i++) {	// ��ԭ�����滻Ϊ���������� 
		numArr[i] = tempArr[i];
	}
	
}

// �鲢����
void mergeSort(int *numArr, int *tempArr, int left, int right) {
	int mid = (left + right)/2;
	
	if(left < right) {
		mergeSort(numArr, tempArr, left, mid);
		mergeSort(numArr, tempArr, mid+1, right);
		merge(numArr, tempArr, left, mid, right);
	}
}

// �ݹ���� 
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
	
	numArr[i] = key;	// ��ʱi��λ�þ��ǻ�׼ֵӦ���ڵ�λ�� 
	
	quickSort(numArr, left, i-1);
	quickSort(numArr, i+1, right);
}

// ��������
void countSort(int *numArr, int n) {
	int min=numArr[0], max=numArr[0];
	int i=0, j=0, range=0;
	
	for(i=1; i<n; i++) {		// ������ֵ��Χ 
		if(numArr[i] > max) {
			max = numArr[i];
		}
		if(numArr[i] < min) {
			min = numArr[i];
		}
	}
	range = max - min + 1;
	
	int *tempArr = (int*)malloc(range * sizeof(int));	// Ϊ������������ڴ�ռ� 
	if(tempArr == NULL) {
		printf("�ڴ����ʧ��");
		return;
	}
	memset(tempArr, 0, sizeof(int) * range);
	
	for(i = 0; i < n; i++) {	// ���� 
		tempArr[numArr[i] - min]++;
	}
	
	j = 0;
	for(i = 0; i < range; i++) {	// �����ְ�˳�����ԭ���� 
		while(tempArr[i]--) {
			numArr[j++] = i+min;
		}
	}
	
	free(tempArr); // �ͷż��������ڴ�	
} 

// ��ȡĳ�����ֵ�ĳһλ
int getDigit(int num, int digit) {
	int i=0, temp=1;
	
	for(i = 1; i< digit; i++) {
		temp *= 10;
	}
	
	return (num/temp)%10;
} 

// ��������
void radixCountSort(int *numArr, int n) {
	int max=numArr[0], maxDigit=0;
	int *buckets[10]; 	// ���𴢴治ͬλ���ϵ����� 
	int i, j, k;
	
	for(i = 1; i < n; i++) {	// �õ������е������ 
		if(numArr[i] > max) {
			max = numArr[i];
		}
	}
	
	while(max != 0) {	// �������λ�� 
		max /= 10;
		maxDigit++;
	}
	
	for(i = 0; i < 10; i++) {
		buckets[i] = (int*)malloc(n*sizeof(int));
	}
	
	int size[10] = {0};		// ÿ��ͰĿǰ�Ĵ�С 
	
	for(i = 1; i <= maxDigit; i++) {	// ��ÿһλ���ֽ������� 
		for( j = 0; j < n; j++) {	// ȷ����λ����ͬʱ�ǰ��մ�С��������� 
			int numDigit = getDigit(numArr[j], i);
			buckets[numDigit][size[numDigit]++] = numArr[j]; 
		}
		
		int index = 0;
		for(j = 0; j < 10; j++) {		// ��ĳλ��������󣬽����������ִ������� 
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


