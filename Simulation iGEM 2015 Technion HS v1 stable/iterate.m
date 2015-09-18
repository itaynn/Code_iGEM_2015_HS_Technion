function Mout = iterate(Min,ts,ddt )
%ITERATE Summary of this function goes here
%   Detailed explanation goes here
Mout(length(Min))=0;
epsilon=1e-4;
for idx=1:length(Min)
    if Min(idx)<0 && ddt(idx)<0
        %Min(idx)=0;
    end
    
    Mout(idx)=Min(idx)+ts*ddt(idx);
    
    %Min(idx)
    %Mout(idx)
end
Mout(3)=Mout(2)*Mout(14);Mout(5)=Mout(4)*Mout(14);

end

