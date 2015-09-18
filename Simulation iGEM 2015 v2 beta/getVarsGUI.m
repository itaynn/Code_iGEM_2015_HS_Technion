function [ total_time,C,V,U,A,B,N_max ,A_0,N_0,tot_a,tot_b, option,solver_var ] = getVarsGUI( hObject,handles )
%GETVARSGUI Summary of this function goes here
%   Detailed explanation goes here
%   option: 1: option (My algorithm/Matlab algorithm); 
%   2: live update
    A_0=get(handles.slider3,'Value');%A_0
    N_0=get(handles.slider4,'Value');%N_0
    tot_a=get(handles.slider5,'Value');%tot_a
    tot_b=get(handles.slider6,'Value');%tot_b
    total_time=get(handles.slider7,'Value');%total_time
    C(1)=10^get(handles.slider8,'Value');%C1
    C(2)=10^get(handles.slider9,'Value');%C2
    C(3)=10^get(handles.slider10,'Value');%C3
    C(4)=10^get(handles.slider11,'Value');%C4
    C(5)=10^get(handles.slider12,'Value');%C5
    C(6)=10^get(handles.slider13,'Value');%C6
    C(7)=10^get(handles.slider14,'Value');%C7
    C(8)=10^get(handles.slider15,'Value');%C8
    C(9)=10^get(handles.slider16,'Value');%C9
    C(10)=10^get(handles.slider17,'Value');%C10
    C(11)=10^get(handles.slider18,'Value');%C11
    C(12)=10^get(handles.slider19,'Value');%C12
    C(13)=10^get(handles.slider20,'Value');%C13
    C(14)=10^get(handles.slider21,'Value');%C14
    C(15)=10^get(handles.slider22,'Value');%C15
    C(16)=10^get(handles.slider23,'Value');%C16
    C(17)=10^get(handles.slider24,'Value');%C17
    C(18)=10^get(handles.slider35,'Value');%C17
    V(1)=10^get(handles.slider27,'Value');%v1
    V(2)=10^get(handles.slider28,'Value');%v2
    U(1)=10^get(handles.slider29,'Value');%u1
    U(2)=10^get(handles.slider30,'Value');%u2
    A=10^get(handles.slider31,'Value');%A_rbs
    B=10^get(handles.slider33,'Value');%B_rbs
    N_max=10^get(handles.slider34,'Value');%N_max
    option(1)=get(handles.popupmenu1,'Value');%option (My algorithm/Matlab algorithm)
     contents1 = cellstr(get(handles.popupmenu3,'String'));
    solver_var= contents1{get(handles.popupmenu3,'Value')};%Matlab ode solver variation
    option(2)=get(handles.checkbox1,'Value');
end

