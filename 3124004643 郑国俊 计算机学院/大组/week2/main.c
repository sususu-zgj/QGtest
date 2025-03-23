#include <stdio.h>
#include <string.h>
#include "CharLinkStack.c"
#include "DoubleLinkStack.c"

int isNum(char c) {		// 判断是否是数字 
	return c >= '0' && c <= '9';
}

int isOperator(char c) {	// 判断是否是运算符 
	return c == '+' || c == '-' || c == '*' || c == '/';
}

int priority(char c) {		// 判断传入的字符的优先级 
	switch (c) {
		case '(': return 0;
		case '+':
		case '-': return 1;
		case '*':
		case '/': return 2;
	}
}

char *toPostFix(char *infix) {		// 将字符串转为后缀表达式 
	CLinkStack *ls = initCLinkStack();
	char *postfix = (char*)malloc((strlen(infix) * 2 - 1) * sizeof(char));		// 处理后得到的后缀表达式 
	int i = 0, j = 0;
	
	while (infix[i] != '\0') {
		if (isNum(infix[i])) {		// 数字，直接输出 
			while (isNum(infix[i])) {
				postfix[j++] = infix[i++];
			}
			postfix[j++] = ' ';		// 数字末尾加上空格 
		}
		else if (isOperator(infix[i])) {	// 运算符，根据情况压入栈顶 
			if (isEmptyC(ls)) {		// 栈为空，直接压入栈顶 
				pushC(ls, infix[i++]);
			}
			else {
				if(priority(getTopC(ls)) >= priority(infix[i])) {	// 优先级小于等于栈顶，弹出栈顶 
					postfix[j++] = popC(ls);
					postfix[j++] = ' ';
				}
				else {		// 优先级大于栈顶，压入栈顶 
					pushC(ls, infix[i++]);
				}
			}
		}
		else if (infix[i] == '(') {		// 左括号，压入栈顶 
			pushC(ls, infix[i++]);
		}
		else if (infix[i] == ')') {		// 右括号，弹出运算符 
			while (1) {
				if (isEmptyC(ls)) {		// 用户输入存在问题：右括号与左括号不匹配 
					return NULL;
				}
				else if (getTopC(ls) == '(') {		// 遇到左括号，终止弹出 
					popC(ls);
					break;
				}
				else {		// 弹出运算符 
					postfix[j++] = popC(ls);
					postfix[j++] = ' ';
				}
			}
			i++;
		}
		else {		// 其他字符，忽略 
			i++;
		}
	}
	
	while(!isEmptyC(ls)) {		// 弹出剩余运算符
		if(getTopC(ls) == '(') {	// 左括号与右括号不对应 
			return NULL;
		}
		postfix[j++] = popC(ls);	
	}
	postfix[j] = '\0';
	
	return postfix;
}

int isCorrect(char *postfix) {		// 判断一个后缀表达式是否正确 
	int numCount = 0;	// 数字的个数 
	int opCount = 0;	// 运算符的个数
	char *token;
	char *string = (char*)malloc(sizeof(postfix));
	
	strcpy(string, postfix);			// 复制postfix，因为strtok()会改变原字符串 
	token = strtok(string, " ");		// 将表达式分割为数个字符串
	
	while (token != NULL) {
		if (isNum(token[0])) {		// 子字符串为数字，由于在转为后缀表达式的过程中，数字与运算符直接必然被' '隔开，所以只需要判断第一位字符 
			numCount++;
		}
		else {		// 由于转化后的后缀表达式只包含数字和运算符，则子字符串不是数字就是运算符 
			opCount++;
		}
		token = strtok(NULL, " ");		// 得到下一个子字符串 
	} 
	
	return numCount == opCount + 1;		// 如果后缀表达式正确，数字个数应该比运算符个数多一 
}

double evaluate(char *postfix) {	// 计算后缀表达式 
	DLinkStack *ls = initDLinkStack();
	char *token = strtok(postfix, " ");		// 将后缀表达式分为多个子字符串 
	double num1, num2, result;		// 用于计算的两个被弹出栈顶的两个数及计算结果 
	
	while(token != NULL) {
		if(isNum(token[0])) {
			pushD(ls, atof(token));		// 是数字，压入栈顶 
		}
		else {
			num1 = popD(ls);		// 位于原中缀表达式运算符右边的数 
			num2 = popD(ls);		// 位于原中缀表达式运算符左边的数 
			
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
					if (num1 == 0.0) {		// 检查被除数是否为0 
						printf("式子在进行除法运算时存在被除数为0(或极其接近0)\n");
						return 9999999.999999;
					}
					result = num2 / num1;
					break;
			}
			pushD(ls, result);		// 计算结果压入栈顶 
		}
		token = strtok(NULL, " ");		// 获取下一个子字符串 
	}
	return popD(ls);	// 返回最终结果 
}

int main() {
	char input[1024];
	char *postfix;
	char c;
	
	while (1) {
		printf("请输入一条四则运算式，其中数字为正整数。式子长度不超过1000\n");
		gets(input);
	
		postfix = toPostFix(input);		// 转化为后缀表达式 
	
		if (postfix == NULL) {		// 在转化过程中是否异常 
			printf("您的输入存在问题，似乎是,请检查并重新输入\n");
			continue;
		}
		
		if (!isCorrect(postfix)) {		// 转化后的表达式是否正确 
			printf("您的输入存在问题，请检查并重新输入\n");
			continue;
		}
		
		//printf("%s\n", postfix);
		printf("式子结果为：%f\n", evaluate(postfix));
		printf("还要再计算吗？如果要请输入1\n");
		
		scanf("%c", &c);	
		if(c == '1') {
			gets(input);	// 清除输入缓存区 
			continue;
		}
		
		return 0;	
	}
}
