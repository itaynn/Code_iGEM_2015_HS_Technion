[total_time,C,V,U,A,B,N_max ,A_0,N_0,tot_a,tot_b, option, solver_var]= getVarsGUI( hObject,handles );

load vars.mat;
[graph,ms,ts]=doSimulation( total_time,C,V,U,A,B,N_max ,A_0,N_0,tot_a,tot_b, option(1),solver_var,axes,colours,selected,mat_names);
