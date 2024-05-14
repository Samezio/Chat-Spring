import { useEffect, useState } from 'react'

export default function useLocalStorage<T>(key: string, defaultValue: T): [T, (newValue:T)=>void] {
    const [value, setValue] = useState<T>(()=>{
        let v = localStorage.getItem(key)
        if(v) {
            return JSON.parse(v);
        }
        return defaultValue;
    });
    useEffect(()=>{
        localStorage.setItem(key, JSON.stringify(value));
    }, [value])
    return [value, setValue]
}
