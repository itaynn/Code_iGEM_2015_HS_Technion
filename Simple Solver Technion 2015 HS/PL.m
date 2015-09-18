
%PL Summary of this function goes here
%   Detailed explanation goes here
timeSpan=150;
load vars.mat

Materials=[0, ... ahl
    A_0, ... ahl out
    0, ... l
    0, ... la
    0, ... la2
    0, ...  aiia
    tot_a, ... a1 
    0,... a2  
    0, ... RNA tet
    0, ... TetR
    tot_b, ... b1
    0, ... b2
    0, ... RNA ccdb
    0,... ccdb
    0, ... X
    N_0, ... N+
    0, ... N-
    ];
opt=odeset('NonNegative',1:17);
Ode(0,0,C,V,U,A,B,N_max);
[T,Y]=ode45(@Ode,[0 timeSpan],Materials,opt);
plot(T,Y(:,10),'-.');%,T,Y(:,14),'-'



