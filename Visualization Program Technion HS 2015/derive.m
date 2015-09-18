function [ D ] = derive( M,C,V,U,A,B,N_max)
%DERIVATIVES Summary of this function goes here
%   Detailed explanation goes here

%    Materials=[
%1   A_0, ... % A_out 	---A
%2     0, ... % aa_in 	---aa
%3     0, ... % aa_out 	---
%4     0, ... % L_in	---L
%5     0, ... % L_out	---
%6     0, ... % LA_out	---LA
%7     0, ... % LA2_out	---LA2
%8     0, ... % a0	---a0
%9     0, ... % a1	---a1
%10    0, ... % TRLV_out---TRLV
%11    0, ... % b0	---b0
%12    0, ... % b1	---b1
%13    0, ... % ccdb_out---ccdb
%14  N_0, ... % N	---N
%15    0, ... % X_out	---X
%     ];

% M(3)=M(2)*M(14);M(5)=M(4)*M(14);
D(15)=0;


%%% version 1/4/15
%1     {dA_{out}}{dt} V
D(1)=-C(1)*M(2)*M(1) - C(2)*M(1)-(C(3)*M(4)*M(1)-C(4)*M(6));
%2     {dLA_{out}}{dt}
D(6)=C(3)*M(4) *M(1) - C(4)*M(6) -2*( C(5)*M(6)^2-C(6)*M(7));
% 3    {dLA_{2,out}}{dt} V
D(7)=C(5)*M(6)^2 - C(6)*M(7) -(C(7)*M(8)*M(7)-C(8)*M(9));
%5     {da_1}{dt} V 
D(9)=C(7)*M(8)*M(7)-C(8)*M(9);
%4     {d(a_0+a_1)}{dt}=0 V
D(8)=-D(9);
%  6   {dTRLV_{out}}{dt} V
D(10)=A*(M(8)*V(1)+M(9)*V(2))-C(9)*M(10)-(C(10)*M(11)*M(10)-C(11)*M(12));
%8     {db_1}{dt} V
D(12)=C(10)*M(11)*M(10) - C(11)*M(12);
%7     {d(b_0 + b_1)}{dt} = 0 V
D(11)=-D(12);
%9     {d{ccdb_{out}}}{dt}= V
D(13)=B * (M(11)*U(1) + M(12)*U(2)) - C(12)*M(13);
%10    {dx_{out}}{dt} = c_{13} N(t)
D(15)=C(13)*M(14);
%11    {d{L_{in}}}{dt}=c_{14}-c_{15}*M(4)+{$\frac{c_4*M(6)}{N(t)}$}
D(4)=C(14)-C(15)*M(4);
%12 --   {L_{out} = N(t)*M(4) 
D(5)=0;
%13     {d{{aa}_{in}}}{dt} = C{16}-C{17}*M(2) V
D(2)= C(16)-C(17)*M(2);
%14  --   {aa_{out} = N(t)*M(2)
D(3)=0;
%15     {dN(t)}{dt}=N(t) (1-\frac{N(t)}{N_{max}})
D(14)=M(14)*(1-M(14)/N_max);

   
end

