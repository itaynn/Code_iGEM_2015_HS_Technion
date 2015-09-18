function  liveUnHold( listeners )
%LIVEUNHOLD Summary of this function goes here
%   Detailed explanation goes here
for i=1:listeners.end
    delete(listeners(i));

end

