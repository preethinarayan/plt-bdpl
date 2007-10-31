// mpeg_reader.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include <fstream>
#include <iostream>
using namespace std;

struct mpeg_packet
{
	void set_bitstream(char in_bitstream[188])
	{
		for(int i=0;i<188;i++)
			data.bitstream[i]=in_bitstream[i];
	}
	void print_data()
	{
		printf("Magic: %d\n", this->data.interpret.magic);
	}

	union data_union
	{
		unsigned char bitstream[188];
		struct interpret_type
		{
			unsigned char magic;
			//valid {0x47} nok {print("Sync Byte Error");};
			unsigned error:1;
			unsigned priority:1;
			unsigned something:1;
			unsigned pid:13;
			unsigned adaptation_field_control:2;
			unsigned transport_scrambling_control:2;
			unsigned continuity_counter:4;
			char payload[184];
		}interpret;
	} data;
};

int _tmain(int argc, _TCHAR* argv[])
{
	ifstream in_file("C:/bb/aa.ts", ios::binary);
	mpeg_packet mpeg_packets[100];
	in_file.seekg (0, ios::beg);

	for(int i=0;in_file.good() && i<100;i++)
	{
		char buffer[188];
		in_file .read(buffer,188);
		mpeg_packets[i].set_bitstream(buffer);
		mpeg_packets[i].print_data();
		
		if(i>0 && mpeg_packets[i].data.interpret.continuity_counter 
			!= (mpeg_packets[i-1].data.interpret.continuity_counter +1)% 16)
			cerr << "Error in continuty in packet "<<i
			<< " [i-1] "<<mpeg_packets[i-1].data.interpret.continuity_counter
			<< " [i] "<<mpeg_packets[i].data.interpret.continuity_counter<<"\n"; 
	}

	char a;
	cin >>a;
	return 0;
}

