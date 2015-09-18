function  g=graph( ms,ts ,axes,colours,selected,mat_names)
%GRAPH Summary of this function goes here
%   Detailed explanation goes here
%hold(axes);
%leg(selected.end)='t';
zoom yon;
figure;
g=plot(axes,ts,ms(1,:),ts,ms(10,:),'g');
%'r',ts,ms(6,:),'b',ts,ms(7,:),'y',ts,ms(13,:),'c',ts,ms(14,:),'m',ts,ms(15,:),'k'
% for i=1:3
% switch(i)
%     case 1:
%         idx=1;
% g=plot(axes,ts,ms(idx,:),'color',colours(idx));
% %leg(i)=mat_names(idx);
% end
%legend(axes,leg);
%legend(axes,'AHL','LA','LA2','TRVL','ccdb','N','X');
 %figure('visible','off'); 
 
 %hz.ActionPreCallback=@zoomIn';
%setAllowAxesZoom(hz,axes,true);
%zoom(axes,'yon');

end

