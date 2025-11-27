#include <iostream>

enum Test { VALUE };

template <typename T> void print(T t) {
	std::cout << "Value is " << t << '\n';
}
template <typename T> void out(T t) {
	print(t);
}

int main() {
	Test x = VALUE;
	out(x);
	return 0;
}
