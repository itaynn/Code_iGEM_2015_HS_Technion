function [ graph ,ms,ts] = doSimulation( total_time,C,V,U,A,B,N_max ,A_0,N_0,tot_a,tot_b, option,string_arg,axes,colours,selected,mat_names)
%DOSIMULATION Summary of this function goes here
%   Detailed explanation goes here
%option -   1 - original simulation
%           2 - matlab simulation
% you must mount first the /GUI library

Materials=[A_0, ... % A_out
    0, ... % aa_in
    0, ... % aa_out
    0, ... % L_in
    0, ... % L_out
    0, ... % LA_out
    0, ... % LA2_out
    tot_a, ... % a0
    0, ... % a1
    0, ... % TRLV_out
    tot_b, ... % b0
    0, ... % b1
    0, ... % ccdb_out
    N_0, ... % N
    0, ... % X_out
    ];
%C(12)=0;
if option == 1
    [graph,ms,ts]=doMySimulation(total_time,C,V,U,A,B,N_max ,Materials,axes,colours,selected,mat_names);
else
    [graph,ms,ts]=doMatSimulation(total_time,C,V,U,A,B,N_max ,Materials,string_arg,axes,colours,selected,mat_names);
end

end

