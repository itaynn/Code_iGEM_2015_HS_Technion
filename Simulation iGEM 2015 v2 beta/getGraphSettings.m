function sts= getGraphSettings( axs)
%GETGRAPHSETTINGS Summary of this function goes here
%   Detailed explanation goes here
xlim=get(axs,'XLim');
ylim=get(axs,'YLim');
sts=[xlim(1),xlim(2),ylim(1),ylim(2)];

end

