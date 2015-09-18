function  g=graph( ms,ts ,colours,selected,mat_names)
%GRAPH Summary of this function goes here
%   Detailed explanation goes here
 figure;
 hold on;
%leg(2)='1';
num1=size(selected);
num=num1(2);
leg=cell(num,1);
for i=1:num
%ts,ms(6,:),'b',ts,ms(7,:),'y',ts,ms(10,:),'g',ts,ms(13,:),'c',ts,ms(14,:),'m',ts,ms(15,:),'k');
idx=selected(i);
colr=colours(idx,:);
g=plot(ts,ms(idx,:),'color',colr);
leg(i)=mat_names(idx);
end
x=leg{:};
legend(leg);
%legend(axes,'AHL','LA','LA2','TRVL','ccdb','N','X');
 %figure('visible','off'); 

 %hz.ActionPreCallback=@zoomIn';
%setAllowAxesZoom(hz,axes,true);
%zoom(axes,'yon');

end

