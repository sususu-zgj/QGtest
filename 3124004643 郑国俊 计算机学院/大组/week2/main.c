#include <stdio.h>
#include <string.h>
#include "CharLinkStack.c"
#include "DoubleLinkStack.c"

int isNum(char c) {		// �ж��Ƿ������� 
	return c >= '0' && c <= '9';
}

int isOperator(char c) {	// �ж��Ƿ�������� 
	return c == '+' || c == '-' || c == '*' || c == '/';
}

int priority(char c) {		// �жϴ�����ַ������ȼ� 
	switch (c) {
		case '(': return 0;
		case '+':
		case '-': return 1;
		case '*':
		case '/': return 2;
	}
}

char *toPostFix(char *infix) {		// ���ַ���תΪ��׺���ʽ 
	CLinkStack *ls = initCLinkStack();
	char *postfix = (char*)malloc((strlen(infix) * 2 - 1) * sizeof(char));		// �����õ��ĺ�׺���ʽ 
	int i = 0, j = 0;
	
	while (infix[i] != '\0') {
		if (isNum(infix[i])) {		// ���֣�ֱ����� 
			while (isNum(infix[i])) {
				postfix[j++] = infix[i++];
			}
			postfix[j++] = ' ';		// ����ĩβ���Ͽո� 
		}
		else if (isOperator(infix[i])) {	// ��������������ѹ��ջ�� 
			if (isEmptyC(ls)) {		// ջΪ�գ�ֱ��ѹ��ջ�� 
				pushC(ls, infix[i++]);
			}
			else {
				if(priority(getTopC(ls)) >= priority(infix[i])) {	// ���ȼ�С�ڵ���ջ��������ջ�� 
					postfix[j++] = popC(ls);
					postfix[j++] = ' ';
				}
				else {		// ���ȼ�����ջ����ѹ��ջ�� 
					pushC(ls, infix[i++]);
				}
			}
		}
		else if (infix[i] == '(') {		// �����ţ�ѹ��ջ�� 
			pushC(ls, infix[i++]);
		}
		else if (infix[i] == ')') {		// �����ţ���������� 
			while (1) {
				if (isEmptyC(ls)) {		// �û�����������⣺�������������Ų�ƥ�� 
					return NULL;
				}
				else if (getTopC(ls) == '(') {		// ���������ţ���ֹ���� 
					popC(ls);
					break;
				}
				else {		// ��������� 
					postfix[j++] = popC(ls);
					postfix[j++] = ' ';
				}
			}
			i++;
		}
		else {		// �����ַ������� 
			i++;
		}
	}
	
	while(!isEmptyC(ls)) {		// ����ʣ�������
		if(getTopC(ls) == '(') {	// �������������Ų���Ӧ 
			return NULL;
		}
		postfix[j++] = popC(ls);	
	}
	postfix[j] = '\0';
	
	return postfix;
}

int isCorrect(char *postfix) {		// �ж�һ����׺���ʽ�Ƿ���ȷ 
	int numCount = 0;	// ���ֵĸ��� 
	int opCount = 0;	// ������ĸ���
	char *token;
	char *string = (char*)malloc(sizeof(postfix));
	
	strcpy(string, postfix);			// ����postfix����Ϊstrtok()��ı�ԭ�ַ��� 
	token = strtok(string, " ");		// �����ʽ�ָ�Ϊ�����ַ���
	
	while (token != NULL) {
		if (isNum(token[0])) {		// ���ַ���Ϊ���֣�������תΪ��׺���ʽ�Ĺ����У������������ֱ�ӱ�Ȼ��' '����������ֻ��Ҫ�жϵ�һλ�ַ� 
			numCount++;
		}
		else {		// ����ת����ĺ�׺���ʽֻ�������ֺ�������������ַ����������־�������� 
			opCount++;
		}
		token = strtok(NULL, " ");		// �õ���һ�����ַ��� 
	} 
	
	return numCount == opCount + 1;		// �����׺���ʽ��ȷ�����ָ���Ӧ�ñ������������һ 
}

double evaluate(char *postfix) {	// �����׺���ʽ 
	DLinkStack *ls = initDLinkStack();
	char *token = strtok(postfix, " ");		// ����׺���ʽ��Ϊ������ַ��� 
	double num1, num2, result;		// ���ڼ��������������ջ������������������ 
	
	while(token != NULL) {
		if(isNum(token[0])) {
			pushD(ls, atof(token));		// �����֣�ѹ��ջ�� 
		}
		else {
			num1 = popD(ls);		// λ��ԭ��׺���ʽ������ұߵ��� 
			num2 = popD(ls);		// λ��ԭ��׺���ʽ�������ߵ��� 
			
			switch (token[0]) {
				case '+':
					result = num1 + num2;
					break;
				case '-':
					result = num2 - num1;
					break;
				case '*':
					result = num1 * num2;
					break;
				case '/':
					if (num1 == 0.0) {		// ��鱻�����Ƿ�Ϊ0 
						printf("ʽ���ڽ��г�������ʱ���ڱ�����Ϊ0(����ӽ�0)\n");
						return 9999999.999999;
					}
					result = num2 / num1;
					break;
			}
			pushD(ls, result);		// ������ѹ��ջ�� 
		}
		token = strtok(NULL, " ");		// ��ȡ��һ�����ַ��� 
	}
	return popD(ls);	// �������ս�� 
}

int main() {
	char input[1024];
	char *postfix;
	char c;
	
	while (1) {
		printf("������һ����������ʽ����������Ϊ��������ʽ�ӳ��Ȳ�����1000\n");
		gets(input);
	
		postfix = toPostFix(input);		// ת��Ϊ��׺���ʽ 
	
		if (postfix == NULL) {		// ��ת���������Ƿ��쳣 
			printf("��������������⣬�ƺ���,���鲢��������\n");
			continue;
		}
		
		if (!isCorrect(postfix)) {		// ת����ı��ʽ�Ƿ���ȷ 
			printf("��������������⣬���鲢��������\n");
			continue;
		}
		
		//printf("%s\n", postfix);
		printf("ʽ�ӽ��Ϊ��%f\n", evaluate(postfix));
		printf("��Ҫ�ټ��������Ҫ������1\n");
		
		scanf("%c", &c);	
		if(c == '1') {
			gets(input);	// ������뻺���� 
			continue;
		}
		
		return 0;	
	}
}
