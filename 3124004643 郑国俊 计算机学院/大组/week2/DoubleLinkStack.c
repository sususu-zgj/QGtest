#include <stdio.h>
#include <stdlib.h>

typedef struct DoubleStackNode {		// Я������Ϊ�ַ�����ջ 
	double data;
	struct DoubleStackNode *next;
}DStackNode, *DLinkStackPtr;

typedef struct DoubleLinkStack {
	DLinkStackPtr top;
	int count;
}DLinkStack;

DLinkStack* initDLinkStack() {		// ��ʼ�� 
	DLinkStack *ls = (DLinkStack*)malloc(sizeof(DLinkStack));
	ls->count = 0;
	ls->top = NULL;
	return ls;
}

void pushD(DLinkStack *ls, double data) {		// ��һ���ַ�ѹ��ջ��
	DLinkStackPtr node = (DLinkStackPtr)malloc(sizeof(DStackNode));
	node->data = data;
	node->next = ls->top;
	ls->top = node;
	ls->count++;
}

int isEmptyD(DLinkStack *ls) {		// ջ�Ƿ�Ϊ�� 
	return ls->count == 0;
}

double popD(DLinkStack *ls) {		// ��һ���ַ���ջ������ 
	DLinkStackPtr temp = ls->top;
	double data = temp->data;
	ls->top = temp->next;
	free(temp);
	ls->count--;
	return data;
}

char getTopD(DLinkStack *ls) {		// �õ�ջ�����ַ� 
	return ls->top->data;
}
