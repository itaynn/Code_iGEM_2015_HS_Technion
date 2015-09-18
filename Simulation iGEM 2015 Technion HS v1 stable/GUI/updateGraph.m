function graph = updateGraph( hObject,handles )
%UPDATEGRAPH Summary of this function goes here
%   Detailed explanation goes here.
% global time_last_call computing;
% 
% 
% if isempty(time_last_call)
%     time_last_call=0;
% else
% time_now=cputime;
% if 0%time_now-time_last_call<0.1
%     retrurmoon;
% end
% time_last_call=time_now;
% if isempty(computing)
% elseif computing==1
%     return;
% end
% 
% computing=1;
axes=handles.axes1;


sts=getGraphSettings(axes);

%if(cr
Number_of_equations=15;
colours=distinguishable_colors(Number_of_equations);
selected=[1,13];
mat_names={'AHL','aa_in','aa_out','L_in','L_out','LA2_out','a0','a1','TRLV','b0','b1','ccdb','N','X'};
[total_time,C,V,U,A,B,N_max ,A_0,N_0,tot_a,tot_b, option, solver_var]= getVarsGUI( hObject,handles );

[graph,ms,ts]=doSimulation( total_time,C,V,U,A,B,N_max ,A_0,N_0,tot_a,tot_b, option(1),solver_var,axes,colours,selected,mat_names);
% if get(handles.checkbox1,'Value')||get(handles.checkbox2,'Value')
%     mode=1;
% else mode =2;
% end
[rngs,peaks_idx,tms_idx]=Analysis(ms,ts,0.05);
fncs= cellstr(get(handles.popupmenu4,'String'));
hold on;
for i=2:length(fncs)
    try
        
        ts=0:0.1:total_time;
        fs=length(ts);
        for j =1:length(ts);
            t=ts(j);
            st=char(fncs(i));
            fs(j)=eval(st);
        end
        
        plot(ts,fs);
    catch err
        warning(getReport(err));
    end
end
hold off;
ctrn=get(handles.checkbox3,'Value');
setGraphSettings(axes,sts,ctrn,total_time);
set(handles.edit5,'String',sts(4));
%computing=0;
end

