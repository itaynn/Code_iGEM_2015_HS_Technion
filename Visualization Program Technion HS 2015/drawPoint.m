function [ graph] = drawPoint( mats,selected,colours,time,names )
%DRAWPOINT Summary of this function goes here
%   Detailed explanation goes here
hold(axes);
leg(selected.end)='t';
st='plot(axes,';
for i=1:selected
    idx=selected(i);
    st=strcat(st,'time,mats(',idx,'),''o'',''color'',colours(',idx,',:),');
    leg(i)=names(idx);
end
st=st(1:(st.end-1));
st=strcat(st,')')
graph=eval(st);
legend(axes,leg);

end

