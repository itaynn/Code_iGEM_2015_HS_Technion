function g = liveUpdate( hObject,eventdata,handles,obj_name )
%LIVEUPDATE Summary of this function goes here
%   Detailed explanation goes here
%tag=hObject.Tag;
%tag=get(hObject,'Tag');
%handles = guidata( ancestor(hObject, 'figure') );
%st=strcat(tag,'_Callback(hObject, eventdata, handles);');
%eval(st);
%st=strcat('handles.',obj_name);
obj=eval(obj_name);
 updateInfo(obj,handles,2)
 if get(handles.checkbox2,'Value')==1
g=updateGraph(obj,handles );
 end
end

