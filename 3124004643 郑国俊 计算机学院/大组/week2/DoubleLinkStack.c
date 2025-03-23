#include <stdio.h>
#include <stdlib.h>

typedef struct DoubleStackNode {		// 携带数据为字符的链栈 
	double data;
	struct DoubleStackNode *next;
}DStackNode, *DLinkStackPtr;

typedef struct DoubleLinkStack {
	DLinkStackPtr top;
	int count;
}DLinkStack;

DLinkStack* initDLinkStack() {		// 初始化 
	DLinkStack *ls = (DLinkStack*)malloc(sizeof(DLinkStack));
	ls->count = 0;
	ls->top = NULL;
	return ls;
}

void pushD(DLinkStack *ls, double data) {		// 将一个字符压入栈顶
	DLinkStackPtr node = (DLinkStackPtr)malloc(sizeof(DStackNode));
	node->data = data;
	node->next = ls->top;
	ls->top = node;
	ls->count++;
}

int isEmptyD(DLinkStack *ls) {		// 栈是否为空 
	return ls->count == 0;
}

double popD(DLinkStack *ls) {		// 将一个字符从栈顶弹出 
	DLinkStackPtr temp = ls->top;
	double data = temp->data;
	ls->top = temp->next;
	free(temp);
	ls->count--;
	return data;
}

char getTopD(DLinkStack *ls) {		// 得到栈顶的字符 
	return ls->top->data;
}
