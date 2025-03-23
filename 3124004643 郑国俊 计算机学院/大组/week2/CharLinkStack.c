#include <stdio.h>
#include <stdlib.h>

typedef struct CharStackNode {		// Я������Ϊ�ַ�����ջ 
	char data;
	struct CharStackNode *next;
}CStackNode, *CLinkStackPtr;

typedef struct CharLinkStack {
	CLinkStackPtr top;
	int count;
}CLinkStack;

CLinkStack* initCLinkStack() {		// ��ʼ�� 
	CLinkStack *ls = (CLinkStack*)malloc(sizeof(CLinkStack));
	ls->count = 0;
	ls->top = NULL;
	return ls;
}

void pushC(CLinkStack *ls, char data) {		// ��һ���ַ�ѹ��ջ�� 
	CLinkStackPtr node = (CLinkStackPtr)malloc(sizeof(CStackNode));
	node->data = data;
	node->next = ls->top;
	ls->top = node;
	ls->count++;
}

int isEmptyC(CLinkStack *ls) {		// ջ�Ƿ�Ϊ�� 
	return ls->count == 0;
}

char popC(CLinkStack *ls) {			// ��һ���ַ���ջ������ 
	CLinkStackPtr temp = ls->top;
	char data = temp->data;
	ls->top = temp->next;
	free(temp);
	ls->count--;
	return data;
}

char getTopC(CLinkStack *ls) {		// �õ�ջ�����ַ� 
	return ls->top->data;
}
