function [ g ,ms,ts] = doMySimulation(total_time,C,V,U,A,B,N_max ,Materials,axes,colours,selected,mat_names)
%DOMYSIMULATION Summary of this function goes here
%   Detailed explanation goes here
%Materials holds the initial condition
time_step=0.01;
itr_num=floor(total_time/time_step);
clear past_Mat;
past_Mat(length(Materials),itr_num)=0;
%cd ..;
%addpath('GUI');
last_plot=0;
times=0:itr_num-1;
times=times*time_step;
plot_period=5;

for inx=1:itr_num
    past_Mat(:,inx)=Materials;
    derivatives=derive(Materials,C,V,U,A,B,N_max);
    Materials=iterate(Materials,time_step,derivatives);
    if times(inx)-last_plot>time_step*plot_period
        %drawPoint(Materials,selected,colours,times(inx),mat_names);
        %last_plot=times(inx);
    end
    
end

g=graph(past_Mat,times,colours,selected,mat_names);

ms=past_Mat;ts=times;

end