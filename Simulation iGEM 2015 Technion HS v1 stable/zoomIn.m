function zoomIn(ax) 
%ZOOMIN Summary of this function goes here
%   Detailed explanation goes here
pre=get(ax,'XLim');
set(ax,'XLim',[0,pre(2)])

end

