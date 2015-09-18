% define constants
% C[i]=1X17 and counting
% C[i]=c_i
% Change defaults in setDefConst()
% V=1X2
% U=1X2
% A,B floats
% N_max integer

%Default: Ido's guess + mends
[C,V,U,A,B,N_max]=setDefConst();


time_step=0.01;
total_time=300;
tot_a=1;% a strings concentration
tot_b=1;% b strings concentration

% external parameters
A_0=1;
N_0=70;



% Materials stracture
Materials=[...
    A_0, ... % A_out 1
    0, ... % aa_in   2
    0, ... % aa_out  3
    0, ... % L_in    4
    0, ... % L_out   5
    0, ... % LA_out  6
    0, ... % LA2_out 7
    tot_a, ... % a0  8
    0, ... % a1      9
    0, ... % TRLV_out10
    tot_b, ... % b0  11
    0, ... % b1      12
    0, ... % ccdb_out13
    N_0, ... % N     14
    0, ... % X_out   15
    ];
itr_num=floor(total_time/time_step);
clear past_Mat;
past_Mat(length(Materials),itr_num)=0;

for inx=1:itr_num
    past_Mat(:,inx)=Materials;
    derivatives=derive(Materials,C,V,U,A,B,N_max);
    Materials=iterate(Materials,time_step,derivatives);
end
times=0:itr_num-1;
times=times*time_step;
graph(past_Mat,times);
