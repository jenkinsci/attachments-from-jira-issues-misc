#include <iostream>
#include <fstream>
#include <string>
#include <algorithm>
#include <stdlib.h>

using namespace std;

int main(int argc, char ** argv)
{
	if (argc != 2)
	{
		cerr << "USAGE: " << argv[0] << " <text file>" << endl;
		exit(EXIT_FAILURE);
	}

	ifstream f;

	f.open(argv[1], ios::in);

	if (!f || !f.is_open())
	{
		cerr << "Could not open file for reading." << endl;
		exit(EXIT_FAILURE);
	}

	string line;
	int errors = 0;
	
	while (getline(f, line))
	{		
		string lstr(line);

		// Line to lower case
		std::transform(lstr.begin(), lstr.end(), lstr.begin(), tolower);

		if ((lstr.find("error") != std::string::npos) || (lstr.find("failed") != std::string::npos))
		{
			cerr << line << endl;
			errors++;
		}
	}

	f.close();

	if (errors != 0)
		exit(EXIT_FAILURE);

	exit(EXIT_SUCCESS);
}