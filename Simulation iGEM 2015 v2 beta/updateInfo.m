function  updateInfo(hObject,handles ,mode)
%UPDATEINFO Summary of this function goes here
%   Detailed explanation goes here
% mode: 1-  External
%       2   Const
%       3   Miscellaneous
str=get(hObject,'Style');
persistent parent;
if strcmp(str,'slider')==1

pos=get(hObject,'Position');
%tex_pos=get(handles.edit1,'Position');
%C1 = get (gca, 'CurrentPoint');

%cursorPoint = get(handles.uipanel3, 'CurrentPoint');
%curX = cursorPoint(1,1);
%curY = cursorPoint(1,2);
val=get(hObject,'Value');
max=get(hObject,'max');
min=get(hObject,'min');
padre=get(hObject,'Parent');
abuelo=get(padre,'Parent');
real_pos=pos+get(padre,'Position')+get(abuelo,'Position');
if mode==1
    editor=handles.edit7;
%     tex_pos=get(editor,'Position');
%     set(editor,'String',(val));
%     %C2=get(0,'PointerLocation');
%     
%     %set(handles.edit1,'Position',[pos(1)+63,pos(2)+39,tex_pos(3),tex_pos(4)]);
%     set(handles.edit1,'Position',[pos(1)+15+((val-min)/(max-min))*41,pos(2)+1.07,tex_pos(3),tex_pos(4)]);
%     %set(handles.edit1,'Position',[30,54.5,tex_pos(3),tex_pos(4)]);
    
elseif mode==2
    editor=handles.edit1;

elseif mode==3
    editor=handles.edit8;
%     tex_pos=get(editor,'Position');
%     set(editor,'String',10^(val));
%     %set(handles.edit1,'Position',[pos(1)+63,pos(2)+32,tex_pos(3),tex_pos(4)]);
%     set(editor,'Position',[pos(1)+15+((val-min)/(max-min))*41,pos(2)+1.07,tex_pos(3),tex_pos(4)]);
end
    tex_pos=get(editor,'Position');
    set(editor,'String',10^(val));
    %set(handles.edit1,'Position',[pos(1)+63,pos(2)+2,tex_pos(3),tex_pos(4)]);
    set(editor,'Position',[pos(1)+7+((val-min)/(max-min))*41,pos(2)+0.4 ,tex_pos(3),tex_pos(4)]);

set(editor,'Visible','on');

parent=hObject;
else
    set(parent,'Value',str2double(get(hObject,'String')));
    
end
if get(handles.checkbox1,'Value')==1
    %[total_time,C,V,U,A,B,N_max ,A_0,N_0,tot_a,tot_b, option, solver_var]= getVarsGUI( hObject,handles );
    %doSimulation( total_time,C,V,U,A,B,N_max ,A_0,N_0,tot_a,tot_b, option(1),solver_var,hObject.axes1);
    updateGraph( hObject,handles );
end


