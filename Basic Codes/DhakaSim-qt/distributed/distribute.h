#ifndef DISTRIBUTE_H
#define DISTRIBUTE_H
#include "others.h"
#include <mpi.h>

class distribute{

public:
    int   id, ntasks, source_id, err, i;
    MPI_Status status;
    distribute(int argc,char* argv[]){

        err = MPI_Init(&argc, &argv); /* Initialize MPI */
        if (err != MPI_SUCCESS) {
            printf("MPI_init failed!\n");
            exit(1);
        }

        err = MPI_Comm_size(MPI_COMM_WORLD, &ntasks);	/* Get nr of tasks */
        if (err != MPI_SUCCESS) {
            printf("MPI_Comm_size failed!\n");
            exit(1);
        }
        //printf("**************%d*******%d\n",MPI_SUCCESS,ntasks);

        err = MPI_Comm_rank(MPI_COMM_WORLD, &id);    /* Get id of this process */
        if (err != MPI_SUCCESS) {
            printf("MPI_Comm_rank failed!\n");
            exit(1);
        }

        /* Check that we run on at least two processors */
        if (ntasks < 1) {
            printf("You have to use at least 2 processes to run this program\n");
            MPI_Finalize();	       /* Quit if there is only one processor */
            exit(0);
        }

        cout<<"MPI Initialized"<<endl;
    }

    void receiveData(int length,vector<int> &linkListShared){
        char name[80];
        int len;
        MPI_Get_processor_name(name, &len);  /* Get name of this processor */
        int tag = 12;
        /* Then receive and print greetings from all other processes */
        int msg[length];
        err = MPI_Recv(msg, length, MPI_INT, MPI_ANY_SOURCE, tag, MPI_COMM_WORLD,&status);          /* Receive a message from any sender */
        if (err != MPI_SUCCESS) {
            printf("Error in MPI_Recv!\n");
            exit(1);
        }
        source_id = status.MPI_SOURCE;	         /* Get id of sender */



        printf("Received in %s PID: %d\n", name,source_id);
        for(int i=0;i<length;i++){
            //cout<<msg[i]<<"   ";
            linkListShared.push_back(msg[i]);
        }
    }
    void sendData(int* a,int length,int i){
        char name[80];
        int len;
        MPI_Get_processor_name(name, &len);  /* Get name of this processor */
        if(id==0) {
            int tag = 12;
            err = MPI_Send(a, length, MPI_INT, i, tag, MPI_COMM_WORLD);
            if (err != MPI_SUCCESS) {
                printf("Process %i: Error in MPI_Send!\n", id);
                exit(1);
            }
            printf("Sending from %s PID: %d ",name,i);
        }

    }
    void getName(){
        char name[80];
        int len;
        MPI_Get_processor_name(name, &len);  /* Get name of this processor */
        cout<<"In PC: "<<name<<"   ";
    }

    ~distribute(){
        err = MPI_Finalize();	         /* Terminate MPI */
        if (err != MPI_SUCCESS) {
            printf("Error in MPI_Finalize!\n");
            exit(1);
        }
    }
};

#endif // DISTRIBUTE_H
