function [ listeners ] = liveHold(hObject, handles )
%LIVEHOLD Summary of this function goes here
%   Detailed explanation goes here
idx=1;
for i=3:34
    if i~=25 && i~=26 && i~=32
        object_name=strcat('handles.slider', num2str(i,'%i'));
        %@(hObject,eventdata)ui_2('pushbutton1_Callback',hObject,eventdata,guidata(hObject))
        %@(hObject,eventdata)ui_2('liveUpdate',hObject,eventdata,guidata(hObject))
        %st=strcat('addlistener(handles.slider', num2str(i,'%i'),',''Value'',''PreSet'',@(hObject,eventdata)ui_2(''liveUpdate'',hObject,eventdata,guidata(hObject)));');%@liveUpdate)');%(~,~)disp(''hi''));');
        %st=strcat('addlistener(handles.slider', num2str(i,'%i'),',''Value'',''PreSet'',@(src,evt)disp(get(src,''value'')));'); handles = guidata( ancestor(hObject, 'figure') )
        %st=strcat('addlistener(handles.slider', num2str(i,'%i'),',''Value'',''PreSet'',@(x,y)liveUpdate(x,y,handles));');%@liveUpdate)');%(~,~)disp(''hi''));');
        st=strcat('addlistener(',object_name,',''Value'',''PostSet'',@(x,y)liveUpdate(x,y,handles,object_name));');
        listeners(idx)=eval(st);
        idx=idx+1;
    end
end

end

