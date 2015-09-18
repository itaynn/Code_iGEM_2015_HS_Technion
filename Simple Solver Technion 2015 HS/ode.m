function D = ode(t, N )
%ODE Summary of this function goes here
%   Detailed explanation goes here
Nmax=2000;
p=1e-4; %probability
Tx=20; %plamid positive division time - minutes
Ty=19.8; %plamid free division time - minutes
D=zeros(3,1);
D(1)=((log(2-p))/Tx)*(1-(N(1)+N(2))/Nmax)*N(1);
D(2)=((log(2)-log(2-p))/Tx)*N(1)+(log(2)/Ty)*(1-(N(1)+N(2))/Nmax)*N(2);
D(3)=D(1)+D(2);
end

