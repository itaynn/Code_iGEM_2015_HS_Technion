% get default parameters
load pretty_parameters.mat
total_time=200;
data(100,100)=0;
time_step=0.01;
for A_0=0.1:0.1:1
[graph,ms,ts]=doSimulation( total_time,C,V,U,A,B,N_max ,A_0,N_0,tot_a,tot_b, option(1),solver_var,axes,colours,selected,mat_names);
for i=1:size(ts,2)
    if i==2000
       lkj=3; 
    end
    dh=(ts(i)/total_time);
    id1=int16(dh*100);
    id2=int16((ms(13,i)/2)*100);
    if(id1>99 || id2>99) continue;
    end
    data(id1+1,id2+1)=data(id1,id2)+0.01;
end
end
%[pks,locs4] = findpeaks(ms(13,:)) ;
%ccdb_peak = [pks(1),locs4(1)*time_step];
%ccdb_at_end=ms(13,end)
%x=1;
%[pks,locs5] = find(ms(13,:)>0.8) ;
%t1 = locs5(1);