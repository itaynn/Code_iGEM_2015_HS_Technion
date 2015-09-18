function [ graph ,ms,ts] = doSimulation( total_time,C,V,U,A,B,N_max ,A_0,N_0,tot_a,tot_b, option,string_arg,axes,colours,selected,mat_names)
%DOSIMULATION Summary of this function goes here
%   Detailed explanation goes here
%option -   1 - original simulation
%           2 - matlab simulation
% you must mount first the /GUI library

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
if option == 1
    [graph,ms,ts]=doMySimulation(total_time,C,V,U,A,B,N_max ,Materials,axes,colours,selected,mat_names);
else
    [graph,ms,ts]=doMatSimulation(total_time,C,V,U,A,B,N_max ,Materials,string_arg,axes,colours,selected,mat_names);
end

end

