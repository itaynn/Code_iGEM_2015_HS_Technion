function  g=graph( ms,ts ,axes,colours,selected,mat_names)
%GRAPH Summary of this function goes here
%   Detailed explanation goes here
hold(axes);
%leg(2)='1';
num1=size(selected);
num=num1(2);
leg=cell(num,1);
hold;
for i=1:num
%ts,ms(6,:),'b',ts,ms(7,:),'y',ts,ms(10,:),'g',ts,ms(13,:),'c',ts,ms(14,:),'m',ts,ms(15,:),'k');
idx=selected(i);
colr=colours(idx,:);
g=plot(axes,ts,ms(idx,:),'color',colr);
leg(i)=mat_names(idx);
end

legend(axes,leg);
%legend(axes,'AHL','LA','LA2','TRVL','ccdb','N','X');
 %figure('visible','off'); 
 
 %hz.ActionPreCallback=@zoomIn';
%setAllowAxesZoom(hz,axes,true);
%zoom(axes,'yon');

end

