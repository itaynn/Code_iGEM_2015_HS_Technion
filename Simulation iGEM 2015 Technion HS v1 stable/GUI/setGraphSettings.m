function setGraphSettings( axes,limits,constrain ,time)
%SETGRAPHSETTINGS Summary of this function goes here
%   Detailed explanation goes here
% mode - 1 - manual zoom
%hz = zoom;
%setAllowAxesZoom(hz,axes,true);
%setAxesZoomMotion(hz,axes,'horizontal');

hz = zoom(axes);

if constrain==0
    axis(axes,limits);
    set(hz,'Motion','both','Enable','on','RightClickAction','InverseZoom');
    
elseif constrain==1
    axis(axes,[0,time,0,limits(4)]);
    set(hz,'Motion','vertical','Enable','on','RightClickAction','InverseZoom');
    
end


end

