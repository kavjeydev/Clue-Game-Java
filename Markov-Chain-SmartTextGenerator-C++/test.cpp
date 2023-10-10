#include <iostream>
#include <cstdlib>
#include <numbers>

using namespace std;

// main()

class shape{

	public:
		virtual double area() = 0;

};

class square : public shape{

	public:

		square(double side_length){
			side_length = side_length;
		}
		double side_length;

		double area(){
			return side_length * side_length;
		}
};

class circle : public shape{

	public:
		circle(double radius){
			radius = radius;
		}

		double radius;

		double area(){
			return (3.14)*(radius * radius);
		}

};

int main(int argc, char* argv[]) {
	cout << "Hello World" << endl;
}
