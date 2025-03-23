#include <stdio.h>
#include <stdlib.h>

typedef struct CharStackNode {		// 携带数据为字符的链栈 
	char data;
	struct CharStackNode *next;
}CStackNode, *CLinkStackPtr;

typedef struct CharLinkStack {
	CLinkStackPtr top;
	int count;
}CLinkStack;

CLinkStack* initCLinkStack() {		// 初始化 
	CLinkStack *ls = (CLinkStack*)malloc(sizeof(CLinkStack));
	ls->count = 0;
	ls->top = NULL;
	return ls;
}

void pushC(CLinkStack *ls, char data) {		// 将一个字符压入栈顶 
	CLinkStackPtr node = (CLinkStackPtr)malloc(sizeof(CStackNode));
	node->data = data;
	node->next = ls->top;
	ls->top = node;
	ls->count++;
}

int isEmptyC(CLinkStack *ls) {		// 栈是否为空 
	return ls->count == 0;
}

char popC(CLinkStack *ls) {			// 将一个字符从栈顶弹出 
	CLinkStackPtr temp = ls->top;
	char data = temp->data;
	ls->top = temp->next;
	free(temp);
	ls->count--;
	return data;
}

char getTopC(CLinkStack *ls) {		// 得到栈顶的字符 
	return ls->top->data;
}
