function [ g ,ms,ts] = doMatSimulation( total_time,C,V,U,A,B,N_max ,Materials,string_arg,axes,colours,selected,mat_names)
%DOMATSIMULATION Summary of this function goes here
%   Detailed explanation goes here
clear T Y;

Ode(0,0,C,V,U,A,B,N_max);
code_string=strcat(string_arg,'(@Ode,[0 , total_time],Materials.'')');
[T,Y] = eval(code_string);%,options);
g=graph(Y.',T,colours,selected,mat_names);
ms=Y.';
ts=T.';

end